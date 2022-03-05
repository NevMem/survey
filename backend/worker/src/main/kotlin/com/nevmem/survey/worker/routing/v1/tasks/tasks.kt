package com.nevmem.survey.worker.routing.v1.tasks

import com.nevmem.survey.task.TaskService
import com.nevmem.survey.worker.api.request.GetTaskRequest
import com.nevmem.survey.worker.api.response.GetTaskResponse
import com.nevmem.surveys.converters.ExportDataTaskConverter
import io.ktor.application.call
import io.ktor.request.receive
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

    post("/task") {
        val request = call.receive<GetTaskRequest>()
        var task = tasksService.getTask(request.taskId)
        call.respond(
            GetTaskResponse(
                task?.let {exportDataTaskConverter(it) }
            )
        )
    }
}
