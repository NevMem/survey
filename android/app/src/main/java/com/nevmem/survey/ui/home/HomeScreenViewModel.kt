package com.nevmem.survey.ui.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.nevmem.survey.data.answer.QuestionAnswer
import com.nevmem.survey.data.question.Question
import com.nevmem.survey.service.survey.SurveyService

sealed class ProgressState {
    data class ActualProgress(val progress: Int, val outOf: Int) : ProgressState() {
        val percentage: Float
            get() = progress.toFloat() / outOf
    }
    object None : ProgressState()
}

enum class HomeScreenActionType {
    Next, Previous, Send,
}

sealed class HomeScreenAction {
    data class Next(val answer: QuestionAnswer) : HomeScreenAction()
    object Previous : HomeScreenAction()
    object Send : HomeScreenAction()
}

data class HomeScreenUiState(
    val currentItem: HomeScreenItem,
    val progress: ProgressState,
    val actions: List<HomeScreenActionType>,
)

class HomeScreenViewModel(
    surveyService: SurveyService,
) : ViewModel() {
    val survey = mutableStateOf(surveyService.survey)

    private var questionIndex = 0
    private val answers: MutableList<QuestionAnswer?> = survey.value.questions.map { null }.toMutableList()

    val uiState = mutableStateOf(
        HomeScreenUiState(
            currentItem = survey.value.questions[questionIndex].buildItem(),
            progress = ProgressState.ActualProgress(1, survey.value.questions.size),
            actions = listOf(HomeScreenActionType.Next),
        )
    )

    private fun next(answer: QuestionAnswer) {
        answers[questionIndex] = answer
        questionIndex += 1

        if (questionIndex in survey.value.questions.indices) {
            uiState.value = HomeScreenUiState(
                currentItem = survey.value.questions[questionIndex].buildItem(),
                progress = ProgressState.ActualProgress(questionIndex + 1, survey.value.questions.size),
                actions = listOf(
                    HomeScreenActionType.Previous,
                    if (questionIndex + 1 in survey.value.questions.indices)
                        HomeScreenActionType.Next
                    else
                        HomeScreenActionType.Send,
                )
            )
        } else {
            uiState.value = HomeScreenUiState(
                currentItem = SendingAnswers.Sending,
                progress = ProgressState.None,
                actions = emptyList(),
            )
        }
    }

    fun dispatch(action: HomeScreenAction) {
        when (action) {
            is HomeScreenAction.Next -> next(action.answer)
        }
    }

    private fun Question.buildItem(): HomeScreenItem = when (this) {
        is Question.RatingQuestion -> RatingQuestion(title, min, max)
        is Question.StarsQuestion -> StarsQuestion(title, stars)
        is Question.TextQuestion -> TextQuestion(title, maxLength)
    }
}
