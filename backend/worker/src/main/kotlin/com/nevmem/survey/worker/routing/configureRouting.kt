package com.nevmem.survey.worker.routing

import com.nevmem.survey.worker.routing.v1.v1Api
import io.ktor.application.Application
import io.ktor.routing.routing

fun Application.configureRouting() {
    routing {
        v1Api()
    }
}
