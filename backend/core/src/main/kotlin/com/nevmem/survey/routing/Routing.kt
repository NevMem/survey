package com.nevmem.survey.routing

import com.nevmem.survey.routing.v1.v1Api
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing

fun Application.configureRouting() {
    routing {
        v1Api()

        get("/ping") {
            call.respondText("pong")
        }
    }
}
