package com.nevmem.survey.routing

import com.nevmem.survey.routing.roles.allRoles
import com.nevmem.survey.routing.v1.v1Api
import com.nevmem.survey.routing.v2.v2Api
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing

fun Application.configureRouting() {
    routing {
        v1Api()
        v2Api()

        allRoles()

        get("/ping") {
            call.respondText("pong")
        }
    }
}
