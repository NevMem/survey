package com.nevmem.survey.data.request.survey

import kotlinx.serialization.Serializable

@Serializable
data class DeleteSurveyRequest(
    val surveyId: Long,
)
