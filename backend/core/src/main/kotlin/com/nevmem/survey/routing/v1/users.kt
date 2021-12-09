package com.nevmem.survey.routing.v1

import io.ktor.application.call
import io.ktor.request.receiveOrNull
import io.ktor.routing.Route
import io.ktor.routing.post

fun Route.users() {
    post("login") {
        val request = call.receiveOrNull<LoginRequest>()
    }
}