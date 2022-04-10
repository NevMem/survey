package com.nevmem.survey.task.internal

import com.nevmem.survey.media.MediaEntity
import com.nevmem.survey.media.MediaStorageService
import com.nevmem.survey.task.ExportDataTaskEntity
import com.nevmem.survey.task.TaskLogEntity
import com.nevmem.survey.task.TaskService
import com.nevmem.survey.task.TaskStateEntity
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal class TaskServiceImpl : TaskService, KoinComponent {

    private val mediaService by inject<MediaStorageService>()

    override suspend fun createExportTask(surveyId: Long): ExportDataTaskEntity = transaction {
        ExportDataTaskDTO.new {
            this.state = TaskStateDTO.Waiting
            this.surveyId = surveyId
            this.medias = ""
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

        if (result != 1) {
            null
        } else {
            taskWithId(entity.id)
        }
    }

    override suspend fun atomicallyTransferToState(
        entity: ExportDataTaskEntity,
        fromState: TaskStateEntity,
        toState: TaskStateEntity,
    ): ExportDataTaskEntity? = transaction {
        val result = ExportDataTaskTable.update({
            (ExportDataTaskTable.surveyId eq entity.surveyId) and
                (ExportDataTaskTable.id eq entity.id) and
                (ExportDataTaskTable.state eq fromState.dtoState())
        }) {
            it[state] = toState.dtoState()
        }

        if (result != 1) {
            null
        } else {
            taskWithId(entity.id)
        }
    }

    override suspend fun appendLog(task: ExportDataTaskEntity, message: String) = transaction<Unit> {
        TaskLogDTO.new {
            this.taskId = task.id
            this.message = message
            this.timestamp = System.currentTimeMillis()
        }
    }

    override suspend fun getExportTask(id: Long): ExportDataTaskEntity? = taskWithId(id)

    override suspend fun attachOutput(task: ExportDataTaskEntity, media: MediaEntity): ExportDataTaskEntity = transaction {
        ExportDataTaskDTO.find {
            ExportDataTaskTable.id eq task.id
        }.first().let {
            it.medias = (it.medias.split(",").filter { it.isNotEmpty() } + media.id.toString()).joinToString(",")
        }

        ExportDataTaskDTO.find {
            ExportDataTaskTable.id eq task.id
        }.first().entity(emptyList())
    }

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
            },
            outputs = this.medias.split(",")
                .filter { it.isNotEmpty() }
                .map { mediaService.mediaById(it.toLong())!! }
        )
    }

    private fun TaskStateEntity.dtoState(): TaskStateDTO {
        return when (this) {
            TaskStateEntity.Error -> TaskStateDTO.Error
            TaskStateEntity.Waiting -> TaskStateDTO.Waiting
            TaskStateEntity.Success -> TaskStateDTO.Success
            TaskStateEntity.Executing -> TaskStateDTO.Executing
        }
    }
}
