package com.nevmem.survey.worker.api

import com.nevmem.survey.data.user.Administrator
import kotlinx.serialization.Serializable

@Serializable
class CreateExportDataTask(
    val user: Administrator,
    val surveyId: Long,
)
