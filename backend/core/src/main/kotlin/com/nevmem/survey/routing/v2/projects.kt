package com.nevmem.survey.routing.v2

import com.nevmem.survey.data.request.invite.CreateInviteRequest
import com.nevmem.survey.data.request.project.CreateProjectRequest
import com.nevmem.survey.data.response.invite.CreateInviteResponse
import com.nevmem.survey.data.response.project.GetProjectsResponse
import com.nevmem.survey.exception.AccessDeniedException
import com.nevmem.survey.exception.NotFoundException
import com.nevmem.survey.invites.InvitesService
import com.nevmem.survey.role.RoleModel
import com.nevmem.survey.routing.toRoles
import com.nevmem.survey.routing.userId
import com.nevmem.survey.survey.ProjectsService
import com.nevmem.survey.users.UsersService
import com.nevmem.surveys.converters.InvitesConverter
import com.nevmem.surveys.converters.ProjectConverter
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
    }
}

fun Route.projects() {
    route("/projects") {
        projectsImpl()
    }
}
