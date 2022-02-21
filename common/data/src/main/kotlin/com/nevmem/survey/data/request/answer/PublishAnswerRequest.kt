package com.nevmem.survey.data.request.answer

import com.nevmem.survey.data.answer.SurveyAnswer
import kotlinx.serialization.Serializable

@Serializable
data class PublishAnswerRequest(
    val answer: SurveyAnswer,
)
