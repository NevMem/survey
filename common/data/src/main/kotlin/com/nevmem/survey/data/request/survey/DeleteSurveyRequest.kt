package com.nevmem.survey.data.request.survey

import com.nevmem.survey.Exported
import kotlinx.serialization.Serializable

@Serializable
@Exported
data class DeleteSurveyRequest(
    val surveyId: Long,
)
