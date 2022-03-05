package com.nevmem.survey.worker.routing.v1.tasks

import com.nevmem.survey.task.TaskService
import com.nevmem.surveys.converters.ExportDataTaskConverter
import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.post
import org.koin.ktor.ext.inject


fun Route.tasks() {
    val tasksService by inject<TaskService>()
    val exportDataTaskConverter by inject<ExportDataTaskConverter>()

    post("/tasks") {
        val tasks = tasksService.exportTasks()
        call.respond(tasks.map { exportDataTaskConverter(it) })
    }
}
