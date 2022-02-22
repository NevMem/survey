package com.nevmem.survey.ui.survey

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.nevmem.survey.data.answer.QuestionAnswer
import com.nevmem.survey.data.question.Question
import com.nevmem.survey.service.survey.SurveyService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
    Next, Previous, Send, Retry,
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
) : ViewModel() {
    val survey = mutableStateOf(surveyService.survey)

    private var questionIndex = 0
    private val answers: MutableList<QuestionAnswer?> = survey.value.questions.map { null }.toMutableList()

    val uiState = mutableStateOf(
        SurveyScreenUiState(
            currentItem = survey.value.questions[questionIndex].buildItem(),
            progress = ProgressState.ActualProgress(1, survey.value.questions.size),
            actions = listOf(SurveyScreenActionType.Next),
        )
    )

    private fun next(answer: QuestionAnswer) {
        answers[questionIndex] = answer
        questionIndex += 1

        assert(questionIndex in survey.value.questions.indices)

        if (questionIndex in survey.value.questions.indices) {
            uiState.value = SurveyScreenUiState(
                currentItem = survey.value.questions[questionIndex].buildItem(),
                progress = ProgressState.ActualProgress(questionIndex + 1, survey.value.questions.size),
                actions = listOf(
                    SurveyScreenActionType.Previous,
                    if (questionIndex + 1 in survey.value.questions.indices)
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
            currentItem = SendingAnswers.Sending,
            progress = ProgressState.None,
            actions = emptyList(),
        )
        background.launch {
            try {
                surveyService.sendAnswer(answers.map { it!! })
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
    }
}
