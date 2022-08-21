package com.nevmem.survey.routing.v2

import com.nevmem.survey.data.request.task.CreateExportDataTaskRequest
import com.nevmem.survey.data.request.task.LoadTaskRequest
import com.nevmem.survey.data.request.task.TasksInSurvey
import com.nevmem.survey.exception.NotFoundException
import com.nevmem.survey.role.RoleModel
import com.nevmem.survey.role.SURVEY_MANAGER
import com.nevmem.survey.routing.toRoles
import com.nevmem.survey.routing.userId
import com.nevmem.survey.survey.ProjectsService
import com.nevmem.survey.survey.SurveysService
import com.nevmem.survey.users.UsersService
import com.nevmem.survey.worker.api.WorkerClient
import com.nevmem.survey.worker.api.request.GetExportDataTasksBySurveyId
import com.nevmem.survey.worker.api.request.GetTaskRequest
import com.nevmem.survey.worker.api.response.CreateExportDataTaskResponse
import com.nevmem.surveys.converters.UsersConverter
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.post
import io.ktor.routing.route
import org.koin.ktor.ext.inject

private const val ACCESS_DENIED = "Access to method denied (not enough roles)"

private fun Route.tasksImpl() {
    val surveysService by inject<SurveysService>()
    val workerApi by inject<WorkerClient>()
    val usersService by inject<UsersService>()
    val projectsService by inject<ProjectsService>()
    val roleModel by inject<RoleModel>()
    val usersConverter by inject<UsersConverter>()

    authenticate {
        post("/create_export_data_task") {
            val request = call.receive<CreateExportDataTaskRequest>()

            val user = usersService.getUserById(userId())!!
            val survey = surveysService.surveyById(request.surveyId) ?: throw NotFoundException("Survey with id ${request.surveyId} not found")
            val project = projectsService.get(survey.projectId) ?: throw IllegalStateException("wtf")
            val userRoles = projectsService.getRolesInProject(project, user)

            if (!roleModel.hasAccess(listOf(SURVEY_MANAGER).toRoles(roleModel), userRoles)) {
                throw IllegalStateException(ACCESS_DENIED)
            }

            val response = workerApi.createExportDataTask(
                com.nevmem.survey.worker.api.request.CreateExportDataTaskRequest(usersConverter(user), request.surveyId)
            )

            when (response) {
                is CreateExportDataTaskResponse.Success -> call.respond(response.task)
                is CreateExportDataTaskResponse.Error -> throw IllegalStateException(response.message)
            }
        }

        post("/export_data_task_by_survey") {
            val request = call.receive<TasksInSurvey>()

            val user = usersService.getUserById(userId())!!
            val survey = surveysService.surveyById(request.surveyId) ?: throw NotFoundException("Survey with id ${request.surveyId} not found")
            val project = projectsService.get(survey.projectId) ?: throw IllegalStateException("wtf")
            val userRoles = projectsService.getRolesInProject(project, user)

            if (!roleModel.hasAccess(listOf(SURVEY_MANAGER).toRoles(roleModel), userRoles)) {
                throw IllegalStateException(ACCESS_DENIED)
            }

            call.respond(workerApi.tasks(GetExportDataTasksBySurveyId(request.surveyId)))
        }

        post("/get") {
            val request = call.receive<LoadTaskRequest>()
            val user = usersService.getUserById(userId())!!

            val task = workerApi.getTask(GetTaskRequest(usersConverter(user), request.id)).task ?: throw NotFoundException("Task not found")

            val project = projectsService.get(task.projectId) ?: throw NotFoundException("Something went wrong")
            val userRoles = projectsService.getRolesInProject(project, user)

            if (!roleModel.hasAccess(listOf(SURVEY_MANAGER).toRoles(roleModel), userRoles)) {
                throw IllegalStateException(ACCESS_DENIED)
            }

            call.respond(task)
        }
    }
}

fun Route.tasks() {
    route("/tasks") {
        tasksImpl()
    }
}
