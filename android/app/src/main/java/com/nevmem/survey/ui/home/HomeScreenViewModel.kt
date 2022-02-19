package com.nevmem.survey.ui.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.nevmem.survey.data.answer.QuestionAnswer
import com.nevmem.survey.data.question.Question
import com.nevmem.survey.service.survey.SurveyService

class HomeScreenViewModel(
    surveyService: SurveyService,
) : ViewModel() {
    val survey = mutableStateOf(surveyService.survey)

    private var questionIndex = 0
    private val answers: MutableList<QuestionAnswer?> = survey.value.questions.map { null }.toMutableList()

    val progress = mutableStateOf(0)
    val currentItem = mutableStateOf(survey.value.questions[questionIndex].buildItem())

    fun next(answer: QuestionAnswer) {
        assert((questionIndex + 1) in survey.value.questions.indices)
        answers[questionIndex] = answer
        questionIndex += 1
        currentItem.value = survey.value.questions[questionIndex].buildItem()
        progress.value = questionIndex
    }

    fun previous() {
    }

    private fun Question.buildItem(): HomeScreenItem = when (this) {
        is Question.RatingQuestion -> RatingQuestion(title, min, max)
        is Question.StarsQuestion -> StarsQuestion(title, stars)
        is Question.TextQuestion -> TextQuestion(title, maxLength)
    }
}
