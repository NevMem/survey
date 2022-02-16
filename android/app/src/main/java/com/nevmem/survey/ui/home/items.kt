package com.nevmem.survey.ui.home

sealed class HomeScreenItem

data class Header(val surveyName: String, val surveyId: String) : HomeScreenItem()

data class TextQuestion(val title: String, val maxLength: Int) : HomeScreenItem()
data class RatingQuestion(val title: String, val min: Int, val max: Int) : HomeScreenItem()
data class StarsQuestion(val title: String, val stars: Int) : HomeScreenItem()
