package com.nevmem.survey.data.response.survey

import com.nevmem.survey.data.survey.Survey
import kotlinx.serialization.Serializable

@Serializable
data class JoinSurveyResponse(
    val survey: Survey,
)
