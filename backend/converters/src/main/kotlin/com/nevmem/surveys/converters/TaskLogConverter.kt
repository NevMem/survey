package com.nevmem.surveys.converters

import com.nevmem.survey.data.task.TaskLog
import com.nevmem.survey.task.TaskLogEntity

class TaskLogConverter {
    operator fun invoke(entity: TaskLogEntity): TaskLog = TaskLog(
        message = entity.message,
        timestamp = entity.timestamp,
    )
}
