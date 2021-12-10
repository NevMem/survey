package com.nevmem.survey.routing.v1

import com.nevmem.survey.data.request.auth.LoginRequest
import com.nevmem.survey.data.response.auth.LoginResponse
import com.nevmem.survey.service.auth.TokenService
import com.nevmem.survey.service.users.UsersService
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receiveOrNull
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.post
import org.koin.ktor.ext.inject

fun Route.users() {
    val usersService by inject<UsersService>()
    val tokenService by inject<TokenService>()

    post("login") {
        val request = call.receiveOrNull<LoginRequest>()
        if (request == null) {
            call.respond(
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
            call.respond(
                HttpStatusCode.Unauthorized,
                LoginResponse.LoginError(
                    error = "User not found",
                )
            )
            return@post
        }

        call.respond(
            LoginResponse.LoginSuccessful(
                token = tokenService.createTokenForUser(user),
            )
        )
    }
}
