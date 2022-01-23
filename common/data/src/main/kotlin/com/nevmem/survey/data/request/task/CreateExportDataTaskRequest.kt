package com.nevmem.survey.data.request.task

import com.nevmem.survey.Exported
import kotlinx.serialization.Serializable

@Exported
@Serializable
data class CreateExportDataTaskRequest(
    val surveyId: Long,
)
