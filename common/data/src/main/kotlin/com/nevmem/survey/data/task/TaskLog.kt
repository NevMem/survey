package com.nevmem.survey.data.task

import com.nevmem.survey.Exported
import kotlinx.serialization.Serializable

@Serializable
@Exported
data class TaskLog(
    val message: String,
    val timestamp: Long,
)
