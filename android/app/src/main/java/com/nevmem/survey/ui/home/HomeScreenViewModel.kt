package com.nevmem.survey.ui.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.nevmem.survey.data.question.Question
import com.nevmem.survey.data.survey.Survey
import com.nevmem.survey.service.survey.SurveyService

class HomeScreenViewModel(
    surveyService: SurveyService,
) : ViewModel() {
    val survey = mutableStateOf(surveyService.survey)

    val items = mutableStateOf(surveyService.survey.buildItems())

    private fun Survey.buildItems(): List<HomeScreenItem> {
        return listOf(Header(name, surveyId)) + questions.map {
            when (it) {
                is Question.RatingQuestion -> RatingQuestion(it.title, it.min, it.max)
                is Question.StarsQuestion -> StarsQuestion(it.title, it.stars)
                is Question.TextQuestion -> TextQuestion(it.title, it.maxLength)
            }
        }
    }
}
