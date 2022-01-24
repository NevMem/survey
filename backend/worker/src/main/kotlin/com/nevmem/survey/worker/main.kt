package com.nevmem.survey.worker

import com.nevmem.survey.task.createTaskService
import com.nevmem.survey.worker.internal.AvailableTasksProvider
import com.nevmem.survey.worker.internal.TaskLocker
import com.nevmem.survey.worker.setup.setupDatabases
import kotlinx.coroutines.runBlocking
import org.koin.core.context.startKoin
import org.koin.dsl.module

private val coreModule = module {
    single { createTaskService() }
}

fun main() {
    setupDatabases()

    startKoin {
        modules(
            coreModule,
        )
    }

    val tasksProvider = AvailableTasksProvider()
    val taskLocker = TaskLocker()

    runBlocking {
        tasksProvider.tasks()
            .collect {
                val task = it.first()

                val lockedTask = taskLocker.tryLockTask(task) ?: return@collect
                println("Successfully locked task $lockedTask")
            }
    }
}
