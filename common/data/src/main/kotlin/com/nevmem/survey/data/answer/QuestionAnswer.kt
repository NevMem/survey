package com.nevmem.survey.data.answer

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class QuestionAnswer {
    @Serializable
    @SerialName("rating")
    data class RatingQuestionAnswer(val number: Int, val commonQuestionId: String? = null) : QuestionAnswer()

    @Serializable
    @SerialName("text")
    data class TextQuestionAnswer(val text: String, val commonQuestionId: String? = null) : QuestionAnswer()

    @Serializable
    @SerialName("stars")
    data class StarsQuestionAnswer(val stars: Int, val commonQuestionId: String? = null) : QuestionAnswer()

    @Serializable
    @SerialName("radio")
    data class RadioQuestionAnswer(val id: String) : QuestionAnswer()
}
