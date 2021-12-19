package com.nevmem.survey.data.request.survey

import com.nevmem.survey.data.question.CommonQuestion
import com.nevmem.survey.data.question.Question
import kotlinx.serialization.Serializable

@Serializable
data class CreateSurveyRequest(
    val name: String,
    val questions: List<Question>,
    val commonQuestions: List<CommonQuestion>,
)
