package com.nevmem.survey.worker.api.request

import kotlinx.serialization.Serializable

@Serializable
data class GetExportDataTasksBySurveyId(
    val surveyId: Long,
)
