package com.nevmem.survey.routing.v2

import com.nevmem.survey.data.project.ProjectAdministratorInfo
import com.nevmem.survey.data.project.ProjectInfo
import com.nevmem.survey.data.request.project.CreateProjectRequest
import com.nevmem.survey.data.request.project.GetProjectInfoRequest
import com.nevmem.survey.data.response.project.GetProjectInfoResponse
import com.nevmem.survey.data.response.project.GetProjectsResponse
import com.nevmem.survey.exception.AccessDeniedException
import com.nevmem.survey.exception.NotFoundException
import com.nevmem.survey.routing.userId
import com.nevmem.survey.survey.ProjectsService
import com.nevmem.survey.users.UsersService
import com.nevmem.surveys.converters.ProjectConverter
import com.nevmem.surveys.converters.RolesConverter
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

private fun Route.projectsImpl() {
    val projectsService by inject<ProjectsService>()
    val usersService by inject<UsersService>()
    val projectConverter by inject<ProjectConverter>()

    val usersConverter by inject<UsersConverter>()
    val rolesConverter by inject<RolesConverter>()

    authenticate {
        post("/create") {
            val request = call.receive<CreateProjectRequest>()
            val user = usersService.getUserById(userId())!!
            val project = projectsService.createProject(request.name, user)
            call.respond(projectConverter(project))
        }

        get("/all") {
            val user = usersService.getUserById(userId())!!
            val projects = projectsService.projects(user)
            call.respond(GetProjectsResponse(projects = projects.map { projectConverter(it) }))
        }

        post("/info") {
            val request = call.receive<GetProjectInfoRequest>()
            val user = usersService.getUserById(userId())!!
            val project = projectsService.get(request.projectId)
                ?: throw NotFoundException("Project with id ${request.projectId} not found")
            if (!projectsService.isUserInvitedToProject(project, user)) {
                throw AccessDeniedException()
            }
            val info = projectsService.getUsersInfo(project)
            call.respond(
                GetProjectInfoResponse(
                    projectInfo = ProjectInfo(
                        administratorsInfo = info.map {
                            ProjectAdministratorInfo(
                                administrator = usersConverter(it.first),
                                roles = it.second.map { role -> rolesConverter(role) },
                            )
                        }
                    )
                )
            )
        }
    }
}

fun Route.projects() {
    route("/projects") {
        projectsImpl()
    }
}
