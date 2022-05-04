package com.nevmem.survey.data.request.survey

import com.nevmem.survey.data.user.UserId
import kotlinx.serialization.Serializable

@Serializable
data class LeaveSurveyRequest(
    val surveyId: String,
    val userId: UserId,
)
