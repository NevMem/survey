package com.nevmem.survey.worker.internal

import com.nevmem.survey.task.ExportDataTaskEntity
import com.nevmem.survey.task.TaskService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal class AvailableTasksProvider : KoinComponent {
    private val tasksService by inject<TaskService>()

    suspend fun tasks(): Flow<List<ExportDataTaskEntity>> = flow {
        while (true) {
            val tasks = tasksService.exportWaitingTasks()
            if (tasks.isNotEmpty()) {
                emit(tasks)
            }
            delay(1000L)
        }
    }
}