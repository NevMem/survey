package com.nevmem.survey.worker.api.request

import com.nevmem.survey.data.user.Administrator
import kotlinx.serialization.Serializable

@Serializable
class CreateExportDataTaskRequest(
    val user: Administrator,
    val surveyId: Long,
)
