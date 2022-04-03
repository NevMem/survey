package com.nevmem.survey.push.routing

import com.nevmem.survey.push.routing.v1.v1Api
import io.ktor.application.Application
import io.ktor.routing.routing

fun Application.configureRouting() {
    routing {
        v1Api()
    }
}
