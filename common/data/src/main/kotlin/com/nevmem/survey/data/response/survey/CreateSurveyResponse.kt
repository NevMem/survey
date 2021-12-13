package com.nevmem.survey.data.response.survey

import com.nevmem.survey.data.survey.Survey
import kotlinx.serialization.Serializable

@Serializable
sealed class CreateSurveyResponse {
    @Serializable
    data class CreateSurveySuccess(
        val survey: Survey,
    ): CreateSurveyResponse()

    @Serializable
    data class CreateSurveyError(
        val message: String,
    ): CreateSurveyResponse()
}
