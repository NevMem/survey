package com.nevmem.survey.routing

import com.nevmem.survey.routing.v1.v1Api
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.configureRouting() {
    routing {
        v1Api()

        get("/ping") {
            call.respondText("pong")
        }
    }
}
