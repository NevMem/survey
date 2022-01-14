package com.nevmem.survey.data.request.answer

import kotlinx.serialization.Serializable

@Serializable
data class LoadAnswersRequest(
    val surveyId: String,
)
