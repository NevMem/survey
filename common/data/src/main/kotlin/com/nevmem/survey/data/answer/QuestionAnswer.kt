package com.nevmem.survey.data.answer

import com.nevmem.survey.Exported
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Exported
sealed class QuestionAnswer {
    @Serializable
    @SerialName("rating")
    data class RatingQuestionAnswer(val number: Int, val commonQuestionId: String? = null) : QuestionAnswer()

    @Serializable
    @SerialName("text")
    data class TextQuestionAnswer(val text: String, val commonQuestionId: String? = null) : QuestionAnswer()

    @Serializable
    @SerialName("stars")
    data class StarsQuestionAnswer(val starts: Int, val commonQuestionId: String? = null) : QuestionAnswer()
}
