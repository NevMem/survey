package com.nevmem.survey.worker.api

import com.nevmem.survey.data.user.Administrator
import kotlinx.serialization.Serializable

@Serializable
data class GetTaskRequest(
    val user: Administrator,
    val taskId: Long,
)
