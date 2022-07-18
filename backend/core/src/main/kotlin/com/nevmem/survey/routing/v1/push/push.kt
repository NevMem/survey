package com.nevmem.survey.routing.v1.push

import com.nevmem.survey.data.request.push.BroadcastAllRequest
import com.nevmem.survey.data.request.push.RegisterPushTokenRequest
import com.nevmem.survey.data.response.push.BroadcastAllResponse
import com.nevmem.survey.data.response.push.RegisterPushTokenResponse
import com.nevmem.survey.push.api.request.RegisterUserRequest
import com.nevmem.survey.push.client.PushClient
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.post
import io.ktor.routing.route
import org.koin.ktor.ext.inject

private fun Route.pushImpl() {
    val pushClient by inject<PushClient>()

    post("/register") {
        val pushTokenRequest: RegisterPushTokenRequest = call.receive()
        pushClient.register(RegisterUserRequest(pushTokenRequest.uid, pushTokenRequest.token))
        call.respond(RegisterPushTokenResponse(pushTokenRequest.token))
    }

    authenticate {
        post("/broadcast") {
            val request: BroadcastAllRequest = call.receive()
            val sent = pushClient.broadcastAll(com.nevmem.survey.push.api.request.BroadcastAllRequest(request.title, request.message))
            call.respond(BroadcastAllResponse(sent.sentMessages))
        }
    }
}

fun Route.push() {
    route("/push") {
        pushImpl()
    }
}
