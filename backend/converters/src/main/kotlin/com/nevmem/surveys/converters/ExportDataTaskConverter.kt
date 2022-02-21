package com.nevmem.surveys.converters

import com.nevmem.survey.data.task.Task
import com.nevmem.survey.data.task.TaskState
import com.nevmem.survey.task.ExportDataTaskEntity
import com.nevmem.survey.task.TaskStateEntity
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ExportDataTaskConverter : KoinComponent {
    private val taskLogConverter by inject<TaskLogConverter>()
    private val mediaConverter by inject<MediaConverter>()

    operator fun invoke(entity: ExportDataTaskEntity): Task = Task(
        id = entity.id,
        state = when (entity.state) {
            TaskStateEntity.Waiting -> TaskState.Waiting
            TaskStateEntity.Executing -> TaskState.Executing
            TaskStateEntity.Success -> TaskState.Success
            TaskStateEntity.Error -> TaskState.Error
        },
        log = entity.log.map { taskLogConverter(it) },
        outputs = entity.outputs.map { mediaConverter(it) }
    )
}
