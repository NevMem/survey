package com.nevmem.survey.data.response.survey

import com.nevmem.survey.Exported
import com.nevmem.survey.data.survey.SurveyMetadata
import kotlinx.serialization.Serializable

@Serializable
@Exported
data class LoadSurveyMetadataResponse(
    val surveyMetadata: SurveyMetadata,
)
