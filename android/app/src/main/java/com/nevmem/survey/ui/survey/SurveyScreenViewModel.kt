package com.nevmem.survey.ui.survey

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.nevmem.survey.data.answer.QuestionAnswer
import com.nevmem.survey.data.question.CommonQuestion
import com.nevmem.survey.data.question.Question
import com.nevmem.survey.service.achievement.api.AchievementService
import com.nevmem.survey.service.camera.CameraDataListener
import com.nevmem.survey.service.survey.SurveyService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

sealed class ProgressState {
    data class ActualProgress(val progress: Int, val outOf: Int) : ProgressState() {
        val percentage: Float
            get() = progress.toFloat() / outOf
    }
    object None : ProgressState()
}

enum class SurveyScreenActionType {
    Next,
    Previous,
    Send,
    Retry,
    TakePicture,
}

sealed class SurveyScreenAction {
    data class Next(val answer: QuestionAnswer) : SurveyScreenAction()
    object Previous : SurveyScreenAction()
    data class Send(val answer: QuestionAnswer) : SurveyScreenAction()
    object Retry : SurveyScreenAction()
}

data class SurveyScreenUiState(
    val currentItem: SurveyScreenItem,
    val progress: ProgressState,
    val actions: List<SurveyScreenActionType>,
)

class SurveyScreenViewModel(
    private val surveyService: SurveyService,
    private val background: CoroutineScope,
    private val achievementService: AchievementService,
    private val cameraDataListener: CameraDataListener,
) : ViewModel() {
    val survey = mutableStateOf(surveyService.survey)

    private val questionsCount
        get() = survey.value.commonQuestions.size + survey.value.questions.size

    private var questionIndex = 0
    private val answers: MutableList<QuestionAnswer?> = (0 until questionsCount)
        .map { null }
        .toMutableList()

    private val medias: MutableList<Uri> = mutableListOf()

    private var job: Job? = null

    val uiState = mutableStateOf(
        SurveyScreenUiState(
            currentItem = buildCurrentQuestionItem(),
            progress = ProgressState.ActualProgress(1, questionsCount),
            actions = listOf(
                SurveyScreenActionType.TakePicture,
                SurveyScreenActionType.Next,
            ),
        )
    )

    init {
        job = background.launch {
            cameraDataListener.uris.collect {
                medias.add(it)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

    private fun buildCurrentQuestionItem(): SurveyScreenItem {
        if (questionIndex in survey.value.commonQuestions.indices) {
            return survey.value.commonQuestions[questionIndex].buildItem()
        }
        return survey.value.questions[questionIndex - survey.value.commonQuestions.size].buildItem()
    }

    private fun next(answer: QuestionAnswer) {
        answers[questionIndex] = answer
        questionIndex += 1

        if (questionIndex < questionsCount) {
            uiState.value = SurveyScreenUiState(
                currentItem = buildCurrentQuestionItem(),
                progress = ProgressState.ActualProgress(questionIndex + 1, questionsCount),
                actions = listOf(
                    SurveyScreenActionType.Previous,
                    SurveyScreenActionType.TakePicture,
                    if (questionIndex + 1 != questionsCount)
                        SurveyScreenActionType.Next
                    else
                        SurveyScreenActionType.Send,
                )
            )
        }
    }

    private fun send(answer: QuestionAnswer) {
        answers[questionIndex] = answer
        sendImpl()
    }

    private fun sendImpl() {
        uiState.value = SurveyScreenUiState(
            currentItem = SendingAnswers.Sending(null),
            progress = ProgressState.None,
            actions = emptyList(),
        )
        background.launch {
            try {
                surveyService.sendAnswer(answers.map { it!! }, medias).collect {
                    uiState.value = SurveyScreenUiState(
                        currentItem = SendingAnswers.Sending(it.progress),
                        progress = ProgressState.None,
                        actions = emptyList(),
                    )
                }
                achievementService.reportSurveyCompleted()
                withContext(Dispatchers.Main) {
                    uiState.value = SurveyScreenUiState(
                        currentItem = SendingAnswers.Success,
                        progress = ProgressState.None,
                        actions = emptyList(),
                    )
                }
            } catch (exception: Exception) {
                withContext(Dispatchers.Main) {
                    uiState.value = SurveyScreenUiState(
                        currentItem = SendingAnswers.Error(exception.message ?: ""),
                        progress = ProgressState.None,
                        actions = listOf(SurveyScreenActionType.Retry),
                    )
                }
            }
        }
    }

    fun dispatch(action: SurveyScreenAction) {
        when (action) {
            is SurveyScreenAction.Next -> next(action.answer)
            is SurveyScreenAction.Send -> send(action.answer)
            is SurveyScreenAction.Retry -> sendImpl()
        }
    }

    private fun Question.buildItem(): SurveyScreenItem = when (this) {
        is Question.RatingQuestion -> RatingQuestion(title, min, max)
        is Question.StarsQuestion -> StarsQuestion(title, stars)
        is Question.TextQuestion -> TextQuestion(title, maxLength)
        is Question.RadioQuestion -> RadioQuestion(
            title = title,
            variants = variants.map { RadioQuestion.QuestionVariant(it.id, it.variant) },
        )
    }

    private fun CommonQuestion.buildItem(): SurveyScreenItem {
        val question: Question = when (id) {
            "school_name" -> Question.TextQuestion("Введите наименование вашего учебного заведения", 512)
            "age" -> Question.RatingQuestion("Введите ваш возраст", 6, 18)
            "region" -> Question.TextQuestion("Из какого вы региона?", 32)
            "grade" -> Question.RatingQuestion("В каком вы классе?", 1, 11)
            else -> throw IllegalStateException("Unsupported Common Question type")
        }
        return question.buildItem()
    }
}
