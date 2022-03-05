package com.nevmem.survey.routing.v1.task

import com.nevmem.survey.data.request.task.CreateExportDataTaskRequest
import com.nevmem.survey.data.request.task.LoadTaskRequest
import com.nevmem.survey.exception.NotFoundException
import com.nevmem.survey.role.RoleModel
import com.nevmem.survey.routing.userId
import com.nevmem.survey.task.TaskService
import com.nevmem.survey.users.UsersService
import com.nevmem.survey.worker.api.createWorkerApi
import com.nevmem.surveys.converters.ExportDataTaskConverter
import com.nevmem.surveys.converters.UsersConverter
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import org.koin.ktor.ext.inject

private fun Route.taskImpl() {
    val taskService by inject<TaskService>()
    val usersService by inject<UsersService>()
    val roleModel by inject<RoleModel>()
    val exportDataTaskConverter by inject<ExportDataTaskConverter>()

    val workerApi = createWorkerApi("http://worker")

    authenticate {
        get("/tasks") {
            val user = usersService.getUserById(userId())!!

            if (!roleModel.hasAccess(listOf(roleModel.roleById("survey.manager")), user.roles)) {
                throw IllegalStateException("Access to method denied (not enough roles)")
            }

//            call.respond(taskService.exportTasks().map { exportDataTaskConverter(it) })
            try {
                call.respond(workerApi.tasks())
            } catch (exception: Exception) {
                println(exception)
            }
        }

        post("/task") {
            val user = usersService.getUserById(userId())!!

            if (!roleModel.hasAccess(listOf(roleModel.roleById("survey.manager")), user.roles)) {
                throw IllegalStateException("Access to method denied (not enough roles)")
            }

            val request = call.receive<LoadTaskRequest>()

            val task = taskService.getTask(request.id) ?: throw NotFoundException()

            call.respond(exportDataTaskConverter(task))
        }

        post("/create_export_data_task") {
            val user = usersService.getUserById(userId())!!

            if (!roleModel.hasAccess(listOf(roleModel.roleById("survey.manager")), user.roles)) {
                throw IllegalStateException("Access to method denied (not enough roles)")
            }

            val request = call.receive<CreateExportDataTaskRequest>()
            val task = taskService.createExportTask(request.surveyId)

            call.respond(exportDataTaskConverter(task))
        }
    }
}

fun Route.task() {
    route("/task") {
        taskImpl()
    }
}
