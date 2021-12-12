package com.nevmem.survey.routing.v1.invites

import com.nevmem.survey.converter.InvitesConverter
import com.nevmem.survey.data.response.invite.GetInvitesResponse
import com.nevmem.survey.service.invites.InvitesService
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.auth.jwt.JWTPrincipal
import io.ktor.auth.principal
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import org.koin.ktor.ext.inject

fun Route.invites() {
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
    }
}
