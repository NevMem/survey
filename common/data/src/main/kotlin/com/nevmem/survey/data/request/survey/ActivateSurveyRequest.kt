package com.nevmem.survey.data.request.survey

import kotlinx.serialization.Serializable

@Serializable
data class ActivateSurveyRequest(
    val surveyId: Long,
)
