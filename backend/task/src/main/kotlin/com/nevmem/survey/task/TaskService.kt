package com.nevmem.survey.task

interface TaskService {
    suspend fun createExportTask(surveyId: Long): ExportDataTaskEntity

    suspend fun exportTasks(): List<ExportDataTaskEntity>

    suspend fun exportWaitingTasks(): List<ExportDataTaskEntity>

    suspend fun atomicallyTransferToExecutingState(entity: ExportDataTaskEntity): ExportDataTaskEntity?

    suspend fun getTask(id: Long): ExportDataTaskEntity?
}