package com.nevmem.survey.data.response.survey

import com.nevmem.survey.Exported
import com.nevmem.survey.data.survey.Survey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Exported
sealed class CreateSurveyResponse {
    @Serializable
    @SerialName("success")
    data class CreateSurveySuccess(
        val survey: Survey,
    ): CreateSurveyResponse()

    @Serializable
    @SerialName("error")
    data class CreateSurveyError(
        val message: String,
    ): CreateSurveyResponse()
}
