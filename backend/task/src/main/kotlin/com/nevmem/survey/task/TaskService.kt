package com.nevmem.survey.task

import com.nevmem.survey.media.MediaEntity

interface TaskService {
    suspend fun createExportTask(surveyId: Long): ExportDataTaskEntity

    suspend fun exportTasks(): List<ExportDataTaskEntity>

    suspend fun exportWaitingTasks(): List<ExportDataTaskEntity>

    suspend fun atomicallyTransferToExecutingState(entity: ExportDataTaskEntity): ExportDataTaskEntity?
    suspend fun atomicallyTransferToState(
        entity: ExportDataTaskEntity,
        fromState: TaskStateEntity,
        toState: TaskStateEntity,
    ): ExportDataTaskEntity?

    suspend fun getExportTask(id: Long): ExportDataTaskEntity?

    suspend fun appendLog(task: ExportDataTaskEntity, message: String)

    suspend fun attachOutput(task: ExportDataTaskEntity, media: MediaEntity): ExportDataTaskEntity
}
