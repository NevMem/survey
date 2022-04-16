package com.nevmem.survey.task.internal

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable

internal enum class TaskStateDTO {
    Waiting,
    Executing,
    Success,
    Error,
}

internal object ExportDataTaskTable : LongIdTable() {
    val state = enumeration("state", TaskStateDTO::class)
    val surveyId = long("surveyId")
    val medias = text("medias")
    val projectId = long("projectId")
}

internal object TaskLogTable : LongIdTable() {
    val taskId = long("taskId")
    val timestamp = long("timestamp")
    val message = text("message")
}

internal class ExportDataTaskDTO(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<ExportDataTaskDTO>(ExportDataTaskTable)

    var state by ExportDataTaskTable.state
    var surveyId by ExportDataTaskTable.surveyId
    var medias by ExportDataTaskTable.medias
    var projectId by ExportDataTaskTable.projectId
}

internal class TaskLogDTO(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<TaskLogDTO>(TaskLogTable)

    var taskId by TaskLogTable.taskId
    var message by TaskLogTable.message
    var timestamp by TaskLogTable.timestamp
}
