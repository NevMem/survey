package com.nevmem.survey.routing.v1

import com.nevmem.surveys.converters.UsersConverter
import com.nevmem.survey.data.request.auth.LoginRequest
import com.nevmem.survey.data.request.auth.RegisterRequest
import com.nevmem.survey.data.request.role.UpdateRolesRequest
import com.nevmem.survey.data.response.auth.LoginResponse
import com.nevmem.survey.data.response.auth.RegisterResponse
import com.nevmem.survey.data.response.managed.ManagedUsersResponse
import com.nevmem.survey.data.response.role.UpdateRolesResponse
import com.nevmem.survey.role.RoleModel
import com.nevmem.survey.routing.userId
import com.nevmem.survey.service.auth.TokenService
import com.nevmem.survey.service.invites.InvitesService
import com.nevmem.survey.service.users.UsersService
import com.nevmem.survey.user.UserEntity
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.auth.jwt.JWTPrincipal
import io.ktor.auth.principal
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.request.receiveOrNull
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import org.koin.ktor.ext.inject

fun Route.users() {
    val usersService by inject<UsersService>()
    val tokenService by inject<TokenService>()
    val invitesService by inject<InvitesService>()
    val usersConverter by inject<UsersConverter>()
    val roleModel by inject<RoleModel>()

    post("/login") {
        val request = call.receiveOrNull<LoginRequest>()
        if (request == null) {
            call.respond<LoginResponse>(
                HttpStatusCode.BadRequest,
                LoginResponse.LoginError(
                    error = "Wrong request format",
                )
            )
            return@post
        }

        val user = usersService.getUserWithCredentials(
            UsersService.Credentials(
                request.login,
                request.password,
            )
        )

        if (user == null) {
            call.respond<LoginResponse>(
                HttpStatusCode.Unauthorized,
                LoginResponse.LoginError(
                    error = "User not found",
                )
            )
            return@post
        }

        call.respond<LoginResponse>(
            LoginResponse.LoginSuccessful(
                token = tokenService.createTokenForUser(user),
            )
        )
    }

    post("/register") {
        val request = call.receiveOrNull<RegisterRequest>()
        if (request == null) {
            call.respond<RegisterResponse>(
                HttpStatusCode.BadRequest,
                RegisterResponse.RegisterError(
                    message = "Wrong request format",
                )
            )
            return@post
        }

        val invite = invitesService.getInviteById(request.inviteId)
        if (invite == null) {
            call.respond<RegisterResponse>(
                HttpStatusCode.NotFound,
                RegisterResponse.RegisterError("Invite not found")
            )
            return@post
        }

        if (invite.expired) {
            call.respond<RegisterResponse>(
                HttpStatusCode.ExpectationFailed,
                RegisterResponse.RegisterError("Invite already expired")
            )
            return@post
        }

        val user = usersService.createUser(
            UsersService.Credentials(
                request.login,
                request.password,
            ),
            UsersService.Personal(
                request.name,
                request.surname,
                request.email,
            ),
            emptyList(),
        )

        invitesService.acceptedBy(invite, user)

        call.respond<RegisterResponse>(RegisterResponse.RegisterSuccessful(tokenService.createTokenForUser(user)))
    }

    authenticate {
        get("/check_auth") {
            call.respond(HttpStatusCode.OK)
        }

        get("/me") {
            val principal = call.principal<JWTPrincipal>()!!
            val user = usersService.getUserById(principal["user_id"]!!.toLong())!!
            call.respond(usersConverter.convertUser(user))
        }

        route("/role") {
            get("/managed_users") {
                val user = usersService.getUserById(userId())!!
                val invites = invitesService.userInvites(user.id)
                val users = mutableListOf<UserEntity>()
                val queue = invites.mapNotNull { it.acceptedBy }.toMutableList()
                while (queue.isNotEmpty()) {
                    val processingUser = queue.removeAt(0)
                    users.add(processingUser)
                    val userInvites = invitesService.userInvites(processingUser.id)
                    userInvites.mapNotNull { it.acceptedBy }.forEach { queue.add(it) }
                }
                call.respond(ManagedUsersResponse(users.map { usersConverter(it) }))
            }

            post("/update_roles") {
                val user = usersService.getUserById(userId())!!
                val request = call.receive<UpdateRolesRequest>()

                if (!roleModel.hasAccess(listOf(roleModel.roleById("role.manager")), user.roles)) {
                    throw IllegalStateException("Access to method denied (not enough roles)")
                }

                val updatingUser =
                    usersService.getUserById(request.user.id) ?: throw IllegalStateException("User not found")

                if (user.id == updatingUser.id) {
                    throw IllegalStateException("You cannot update your own roles")
                }

                usersService.updateUserRoles(updatingUser, request.roles.map { roleModel.roleById(it.id) })

                call.respond(UpdateRolesResponse(request.user, request.roles))
            }
        }
    }
}
