package com.nevmem.survey.task.internal

import com.nevmem.survey.task.ExportDataTaskEntity
import com.nevmem.survey.task.TaskLogEntity
import com.nevmem.survey.task.TaskService
import com.nevmem.survey.task.TaskStateEntity
import org.jetbrains.exposed.sql.transactions.transaction

internal class TaskServiceImpl : TaskService {
    override suspend fun createExportTask(surveyId: Long): ExportDataTaskEntity = transaction {
        ExportDataTaskDTO.new {
            this.state = TaskStateDTO.Waiting
            this.surveyId = surveyId
        }.entity(emptyList())
    }

    override suspend fun exportTasks(): List<ExportDataTaskEntity> = transaction {
        ExportDataTaskDTO.all()
            .map {
                val log = TaskLogDTO.find {
                    TaskLogTable.taskId eq it.id.value
                }
                it.entity(log.toList())
            }
    }

    private fun ExportDataTaskDTO.entity(log: List<TaskLogDTO>): ExportDataTaskEntity {
        return ExportDataTaskEntity(
            id = this.id.value,
            state = when (this.state) {
                TaskStateDTO.Waiting -> TaskStateEntity.Waiting
                TaskStateDTO.Executing -> TaskStateEntity.Executing
                TaskStateDTO.Success -> TaskStateEntity.Success
                TaskStateDTO.Error -> TaskStateEntity.Error
            },
            surveyId = this.surveyId,
            log = log.map {
                TaskLogEntity(
                    message = it.message,
                    timestamp = it.timestamp,
                )
            }
        )
    }
}
