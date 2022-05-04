package com.nevmem.survey.routing.v1

import com.nevmem.survey.auth.TokenService
import com.nevmem.survey.data.request.auth.LoginRequest
import com.nevmem.survey.data.response.auth.LoginResponse
import com.nevmem.survey.users.UsersService
import com.nevmem.surveys.converters.UsersConverter
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.auth.jwt.JWTPrincipal
import io.ktor.auth.principal
import io.ktor.http.HttpStatusCode
import io.ktor.request.receiveOrNull
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import org.koin.ktor.ext.inject

fun Route.users() {
    val usersService by inject<UsersService>()
    val tokenService by inject<TokenService>()
    val usersConverter by inject<UsersConverter>()

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

    authenticate {
        get("/check_auth") {
            call.respond(HttpStatusCode.OK)
        }

        get("/me") {
            val principal = call.principal<JWTPrincipal>()!!
            val user = usersService.getUserById(principal["user_id"]!!.toLong())!!
            call.respond(usersConverter.convertUser(user))
        }
    }
}
