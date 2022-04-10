package com.nevmem.survey.routing.v1.invites

import com.nevmem.survey.data.response.invite.GetInvitesResponse
import com.nevmem.survey.invites.InvitesService
import com.nevmem.surveys.converters.InvitesConverter
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.auth.jwt.JWTPrincipal
import io.ktor.auth.principal
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.route
import org.koin.ktor.ext.inject

private fun Route.invitesImpl() {
    val invitesService by inject<InvitesService>()
    val invitesConverter by inject<InvitesConverter>()

    authenticate {
        get("/my_invites") {
            val user = call.principal<JWTPrincipal>()!!
            val userId = user["user_id"]!!

            val invites = invitesService.userInvites(userId.toLong())

            call.respond(
                GetInvitesResponse(
                    invites = invites.map { invitesConverter(it) }
                )
            )
        }

        /* post("/create_invite") {
            try {
                val principal = call.principal<JWTPrincipal>()!!
                val userId = principal["user_id"]!!.toLong()

                val user = usersService.getUserById(userId)!!

                val request = call.receive<CreateInviteRequest>()

                if (!roleModel.hasAccess(listOf(roleModel.roleById("invite.manager")), user.roles)) {
                    throw IllegalStateException("Access to method denied (not enough roles)")
                }

                val invite = invitesService.createInvite(
                    user,
                    request.expirationTimeSeconds
                )

                call.respond<CreateInviteResponse>(
                    CreateInviteResponse.CreateInviteSuccess(
                        invitesConverter(invite)
                    )
                )
            } catch (ex: Exception) {
                call.respond<CreateInviteResponse>(
                    CreateInviteResponse.CreateInviteError(
                        message = ex.message ?: "Unknown error"
                    )
                )
            }
        } */
    }
}

fun Route.invites() {
    route("/invite") {
        invitesImpl()
    }
}
