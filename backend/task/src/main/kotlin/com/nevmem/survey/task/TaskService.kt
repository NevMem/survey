package com.nevmem.survey.task

interface TaskService {
    suspend fun createExportTask(surveyId: Long): ExportDataTaskEntity

    suspend fun exportTasks(): List<ExportDataTaskEntity>
}
