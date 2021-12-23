package com.nevmem.survey.data.response.survey

import com.nevmem.survey.Exported
import com.nevmem.survey.data.survey.Survey
import kotlinx.serialization.Serializable

@Serializable
@Exported
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
