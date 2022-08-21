package com.nevmem.survey.worker.api.response

import com.nevmem.survey.data.task.Task
import kotlinx.serialization.Serializable

@Serializable
data class GetTaskResponse(
    val task: Task?,
)
