package com.nevmem.survey.task.internal

import com.nevmem.survey.task.ExportDataTaskEntity
import com.nevmem.survey.task.TaskService

internal class TaskServiceImpl : TaskService {
    override suspend fun createExportTask(surveyId: Long): ExportDataTaskEntity {
        TODO("Not yet implemented")
    }

    override suspend fun exportTasks(): List<ExportDataTaskEntity> {
        TODO("Not yet implemented")
    }
}