package com.nevmem.survey.worker.internal

import io.ktor.application.Application
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.ktor.ext.inject

fun Application.initLogic() {
    val tasksProvider by inject<AvailableTasksProvider>()
    val taskLocker by inject<TaskLocker>()
    val exporter = Exporter()

    GlobalScope.launch {
        tasksProvider.tasks()
            .collect {
                val task = it.first()

                val lockedTask = taskLocker.tryLockTask(task) ?: return@collect
                println("Successfully locked task $lockedTask")

                exporter.runExportTask(lockedTask)
            }
    }
}
