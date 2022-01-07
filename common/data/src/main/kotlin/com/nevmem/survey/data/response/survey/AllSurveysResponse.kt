package com.nevmem.survey.data.response.survey

import com.nevmem.survey.Exported
import com.nevmem.survey.data.survey.Survey
import kotlinx.serialization.Serializable

@Serializable
@Exported
data class AllSurveysResponse(
    val surveys: List<Survey>,
)
