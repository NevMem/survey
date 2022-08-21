package com.nevmem.survey.worker.api.response

import com.nevmem.survey.data.task.Task
import kotlinx.serialization.Serializable

@Serializable
sealed class CreateExportDataTaskResponse {
    @Serializable
    data class Success(val task: Task) : CreateExportDataTaskResponse()

    @Serializable
    data class Error(val message: String) : CreateExportDataTaskResponse()
}
