package com.nevmem.survey.routing.v1

import com.nevmem.survey.data.request.auth.LoginRequest
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receiveOrNull
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.post

fun Route.users() {
    post("login") {
        val request = call.receiveOrNull<LoginRequest>()
        if (request == null) {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }


    }
}