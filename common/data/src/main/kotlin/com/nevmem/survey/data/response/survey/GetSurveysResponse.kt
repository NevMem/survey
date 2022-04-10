package com.nevmem.survey.data.response.survey

import com.nevmem.survey.Exported
import com.nevmem.survey.data.survey.Survey
import kotlinx.serialization.Serializable

@Exported
@Serializable
data class GetSurveysResponse(
    val projectId: Long,
    val surveys: List<Survey>,
)
