package com.nevmem.survey.data.task

import com.nevmem.survey.Exported
import kotlinx.serialization.Serializable

@Serializable
enum class TaskState {
    Waiting,
    Executing,
    Success,
    Error,
}

@Exported
@Serializable
data class Task(
    val id: Long,
    val state: TaskState,
    val log: List<TaskLog>,
)
