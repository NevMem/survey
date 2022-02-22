package com.nevmem.survey.ui.survey

sealed class SurveyScreenItem

data class TextQuestion(val title: String, val maxLength: Int) : SurveyScreenItem()
data class RatingQuestion(val title: String, val min: Int, val max: Int) : SurveyScreenItem()
data class StarsQuestion(val title: String, val stars: Int) : SurveyScreenItem()

sealed class SendingAnswers : SurveyScreenItem() {
    object Sending : SendingAnswers()
    data class Error(val message: String) : SendingAnswers()
    object Success : SendingAnswers()
}
