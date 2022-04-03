package com.nevmem.survey.push.routing.v1

import com.nevmem.survey.push.api.request.BroadcastAllRequest
import com.nevmem.survey.push.api.request.RegisterUserRequest
import com.nevmem.survey.push.api.response.BroadcastAllResponse
import com.nevmem.survey.push.api.response.RegisterUserResponse
import com.nevmem.survey.push.service.data.PushDataService
import com.nevmem.survey.push.service.push.PushService
import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.post
import org.koin.ktor.ext.inject

fun Route.pushes() {
    val pushDataService by inject<PushDataService>()
    val pushService by inject<PushService>()

    post("/register") {
        val request = call.receive<RegisterUserRequest>()
        pushDataService.register(request.userId, request.token)
        call.respond(RegisterUserResponse(request.userId))
    }

    post("/broadcast") {
        val request = call.receive<BroadcastAllRequest>()
        val sent = pushService.broadcastAll(request.title, request.message)
        call.respond(BroadcastAllResponse(sent))
    }
}
