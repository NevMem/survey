package com.nevmem.survey.routing.v2

import com.nevmem.survey.data.project.ProjectAdministratorInfo
import com.nevmem.survey.data.project.ProjectInfo
import com.nevmem.survey.data.request.project.CreateProjectRequest
import com.nevmem.survey.data.request.project.GetProjectInfoRequest
import com.nevmem.survey.data.request.project.GetProjectRequest
import com.nevmem.survey.data.request.project.UpdateUserRoles
import com.nevmem.survey.data.response.project.GetProjectInfoResponse
import com.nevmem.survey.data.response.project.GetProjectResponse
import com.nevmem.survey.data.response.project.GetProjectsResponse
import com.nevmem.survey.exception.AccessDeniedException
import com.nevmem.survey.exception.NotFoundException
import com.nevmem.survey.role.RoleModel
import com.nevmem.survey.routing.toRoles
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
    val roleModel by inject<RoleModel>()

    val usersConverter by inject<UsersConverter>()
    val rolesConverter by inject<RolesConverter>()

    authenticate {
        post("/create") {
            val request = call.receive<CreateProjectRequest>()
            val user = usersService.getUserById(userId())!!
            val project = projectsService.createProject(request.name, user)
            call.respond(projectConverter(project))
        }

        post("/get") {
            val request = call.receive<GetProjectRequest>()
            val user = usersService.getUserById(userId())!!
            val project = projectsService.get(request.id)
                ?: throw NotFoundException("Project with id ${request.id} not found")
            if (projectsService.isUserInvitedToProject(project, user)) {
                call.respond(GetProjectResponse(projectConverter(project)))
            }
            throw AccessDeniedException()
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

        post("/update_user_roles") {
            val request = call.receive<UpdateUserRoles>()

            val user = usersService.getUserById(userId())!!
            val project = projectsService.get(request.projectId)
                ?: throw NotFoundException("Project with id ${request.projectId} not found")
            val userRoles = projectsService.getRolesInProject(project, user)

            if (!roleModel.hasAccess(listOf("admin").toRoles(roleModel), userRoles)) {
                throw IllegalStateException("Access to method denied (not enough roles)")
            }

            val userToUpdate = usersService.getUserById(request.user.id) ?: throw NotFoundException("User not found")

            projectsService.updateUserRoles(
                project,
                userToUpdate,
                request.roles.map { roleModel.roleById(it.id) },
            )

            call.respond(Unit)
        }
    }
}

fun Route.projects() {
    route("/projects") {
        projectsImpl()
    }
}
