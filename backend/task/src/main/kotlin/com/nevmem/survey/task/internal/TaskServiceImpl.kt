package com.nevmem.survey.task.internal

import com.nevmem.survey.task.ExportDataTaskEntity
import com.nevmem.survey.task.TaskLogEntity
import com.nevmem.survey.task.TaskService
import com.nevmem.survey.task.TaskStateEntity
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

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

    override suspend fun exportWaitingTasks(): List<ExportDataTaskEntity> = transaction {
        ExportDataTaskDTO.find {
            ExportDataTaskTable.state eq TaskStateDTO.Waiting
        }.map { it.entity(emptyList()) }
    }

    override suspend fun atomicallyTransferToExecutingState(entity: ExportDataTaskEntity): ExportDataTaskEntity? = transaction {
        val result = ExportDataTaskTable.update({
            (ExportDataTaskTable.surveyId eq entity.surveyId) and
                (ExportDataTaskTable.id eq entity.id) and
                (ExportDataTaskTable.state eq TaskStateDTO.Waiting)
        }) {
            it[state] = TaskStateDTO.Executing
        }

        println("Value after update $result")

        if (result != 1) {
            null
        } else {
            taskWithId(entity.id)
        }
    }

    override suspend fun getTask(id: Long): ExportDataTaskEntity? = taskWithId(id)

    private fun taskWithId(id: Long) = transaction {
        val log = TaskLogDTO.find {
            TaskLogTable.taskId eq id
        }
        ExportDataTaskDTO.find {
            ExportDataTaskTable.id eq id
        }.firstOrNull()?.entity(log.toList())
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
