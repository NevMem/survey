package com.nevmem.survey.worker.internal

import com.nevmem.survey.task.ExportDataTaskEntity
import com.nevmem.survey.task.TaskService
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal class TaskLocker : KoinComponent {
    private val taskService by inject<TaskService>()

    suspend fun tryLockTask(entity: ExportDataTaskEntity): ExportDataTaskEntity? {
        return taskService.atomicallyTransferToExecutingState(entity)
    }
}
