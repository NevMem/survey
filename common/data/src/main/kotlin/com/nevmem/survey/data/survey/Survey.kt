package com.nevmem.survey.data.survey

import com.nevmem.survey.data.question.CommonQuestion
import com.nevmem.survey.data.question.Question
import kotlinx.serialization.Serializable

@Serializable
data class Survey(
    val id: Long,
    val name: String,
    val questions: List<Question>,
    val commonQuestions: List<CommonQuestion>,
    val active: Boolean,
)
