package com.nevmem.survey.routing.v1.task

import io.ktor.auth.authenticate
import io.ktor.routing.Route
import io.ktor.routing.route

private fun Route.taskImpl() {
//    val usersService by inject<UsersService>()
//    val roleModel by inject<RoleModel>()
//    val userConverter by inject<UsersConverter>()
//    val workerApi by inject<WorkerApi>()

    authenticate {
        /* get("/tasks") {
            val user = usersService.getUserById(userId())!!

            if (!roleModel.hasAccess(listOf(roleModel.roleById("survey.manager")), user.roles)) {
                throw IllegalStateException("Access to method denied (not enough roles)")
            }

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

            call.respond(workerApi.getTask(userConverter(user), request.id))
        }

        post("/create_export_data_task") {
            val user = usersService.getUserById(userId())!!

            if (!roleModel.hasAccess(listOf(roleModel.roleById("survey.manager")), user.roles)) {
                throw IllegalStateException("Access to method denied (not enough roles)")
            }

            val request = call.receive<CreateExportDataTaskRequest>()
            call.respond(workerApi.createExportDataTask(userConverter(user), request.surveyId))
        } */
    }
}

fun Route.task() {
    route("/task") {
        taskImpl()
    }
}
