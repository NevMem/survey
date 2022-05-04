package com.nevmem.survey.data.request.survey

import com.nevmem.survey.data.user.UserId
import kotlinx.serialization.Serializable

@Serializable
data class JoinSurveyRequest(
    val surveyId: String,
    val userId: UserId,
)
