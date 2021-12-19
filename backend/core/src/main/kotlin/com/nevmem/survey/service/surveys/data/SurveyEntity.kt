package com.nevmem.survey.service.surveys.data

data class SurveyEntity(
    val id: Long,
    val name: String,
    val questions: List<QuestionEntity>,
    val commonQuestions: List<CommonQuestionEntity>,
    val active: Boolean,
)
