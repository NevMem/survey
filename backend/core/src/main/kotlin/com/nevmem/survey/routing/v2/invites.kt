package com.nevmem.survey.routing.v2

import com.nevmem.survey.data.request.invite.AcceptInviteRequest
import com.nevmem.survey.data.request.invite.CreateInviteRequest
import com.nevmem.survey.data.response.invite.AcceptInviteResponse
import com.nevmem.survey.data.response.invite.AcceptInviteStatus
import com.nevmem.survey.data.response.invite.CreateInviteResponse
import com.nevmem.survey.data.response.invite.IncomingInvitesResponse
import com.nevmem.survey.data.response.invite.OutgoingInvitesResponse
import com.nevmem.survey.exception.AccessDeniedException
import com.nevmem.survey.exception.NotFoundException
import com.nevmem.survey.invite.InviteEntityStatus
import com.nevmem.survey.invites.InvitesService
import com.nevmem.survey.role.RoleModel
import com.nevmem.survey.routing.toRoles
import com.nevmem.survey.routing.userId
import com.nevmem.survey.survey.ProjectsService
import com.nevmem.survey.users.UsersService
import com.nevmem.surveys.converters.InvitesConverter
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.post
import io.ktor.routing.route
import org.koin.ktor.ext.inject

private fun Route.invitesImpl() {
    val roleModel by inject<RoleModel>()
    val invitesService by inject<InvitesService>()
    val invitesConverter by inject<InvitesConverter>()
    val projectsService by inject<ProjectsService>()
    val usersService by inject<UsersService>()

    authenticate {
        post("/create") {
            val user = usersService.getUserById(userId())!!
            val request = call.receive<CreateInviteRequest>()
            val project = projectsService.get(request.projectId)
                ?: throw NotFoundException("Project with id ${request.projectId} not found")
            val roles = projectsService.getRolesInProject(project, user)
            if (!roleModel.hasAccess(listOf("invite.manager").toRoles(roleModel), roles)) {
                throw AccessDeniedException()
            }

            val toUser = usersService.getUserByLogin(request.userLogin)
                ?: throw NotFoundException("User with login ${request.userLogin} not found")

            val invite = invitesService.createInvite(
                fromUser = user,
                toUser = toUser,
                project = project,
                expirationSeconds = request.expirationTimeSeconds
            )

            call.respond(CreateInviteResponse(invitesConverter(invite)))
        }

        post("/incoming") {
            val user = usersService.getUserById(userId())!!
            val incoming = invitesService.incomingInvites(user)
            call.respond(IncomingInvitesResponse(incoming.map { invitesConverter(it) }))
        }

        post("/outgoing") {
            val user = usersService.getUserById(userId())!!
            val incoming = invitesService.outgoingInvites(user)
            call.respond(OutgoingInvitesResponse(incoming.map { invitesConverter(it) }))
        }

        post("/accept") {
            val request = call.receive<AcceptInviteRequest>()
            val user = usersService.getUserById(userId())!!
            val invite = invitesService.get(request.id) ?: throw NotFoundException("Invite with id ${request.id} not found")

            if (invite.status == InviteEntityStatus.Accepted) {
                throw IllegalStateException("Cannot accept already accepted invite")
            }

            if (invite.status == InviteEntityStatus.Expired) {
                call.respond(AcceptInviteResponse(AcceptInviteStatus.Expired))
                return@post
            }

            assert(invite.status == InviteEntityStatus.Waiting)

            if (invite.toUser != user) {
                throw AccessDeniedException()
            }

            projectsService.addUserToProject(invite.project, user)

            invitesService.accept(invite)

            call.respond(AcceptInviteResponse(AcceptInviteStatus.Ok))
        }
    }
}

fun Route.invites() {
    route("/invites") {
        invitesImpl()
    }
}
