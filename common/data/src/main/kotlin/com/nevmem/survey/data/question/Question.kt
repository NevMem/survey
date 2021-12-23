package com.nevmem.survey.data.question

import com.nevmem.survey.Exported
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Exported
sealed class Question {
    @SerialName("rating")
    @Serializable
    data class RatingQuestion(
        val title: String,
        val min: Int,
        val max: Int,
    ) : Question()

    @SerialName("stars")
    @Serializable
    data class StarsQuestion(
        val title: String,
        val stars: Int,
    ) : Question()

    @SerialName("text")
    @Serializable
    data class TextQuestion(
        val title: String,
        val maxLength: Int,
    ) : Question()
}
