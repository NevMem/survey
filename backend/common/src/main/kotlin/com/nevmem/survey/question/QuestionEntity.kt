package com.nevmem.survey.question

sealed class QuestionEntity {
    data class RatingQuestionEntity(
        val id: Long,
        val title: String,
        val min: Int,
        val max: Int,
    ) : QuestionEntity()

    data class StarsQuestionEntity(
        val id: Long,
        val title: String,
        val stars: Int,
    ) : QuestionEntity()

    data class TextQuestionEntity(
        val id: Long,
        val title: String,
        val maxLength: Int,
    ) : QuestionEntity()

    data class RadioQuestionEntity(
        val id: Long,
        val title: String,
        val variants: List<QuestionVariantEntity>,
    ) : QuestionEntity()
}

data class QuestionVariantEntity(
    val id: String,
    val variant: String,
)
