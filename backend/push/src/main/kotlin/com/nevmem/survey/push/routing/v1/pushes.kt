package com.nevmem.survey.push.routing.v1

import com.nevmem.survey.push.api.request.RegisterUserRequest
import com.nevmem.survey.push.api.response.RegisterUserResponse
import com.nevmem.survey.push.service.PushDataService
import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.post
import org.koin.ktor.ext.inject

fun Route.pushes() {
    val pushService by inject<PushDataService>()

    post("/register") {
        val request = call.receive<RegisterUserRequest>()
        pushService.register(request.userId, request.token)
        call.respond(RegisterUserResponse(request.userId))
    }
}
