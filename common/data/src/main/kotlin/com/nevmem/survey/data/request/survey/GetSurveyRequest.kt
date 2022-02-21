package com.nevmem.survey.data.request.survey

import com.nevmem.survey.Exported
import kotlinx.serialization.Serializable

@Exported
@Serializable
data class GetSurveyRequest(
    val surveyId: String,
)
