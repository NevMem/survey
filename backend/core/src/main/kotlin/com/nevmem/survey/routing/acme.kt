package com.nevmem.survey.routing

import com.nevmem.survey.env.CoreEnvVars
import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.route

fun Route.acme() {
    route("/.well-known/acme-challenge/") {
        val key = CoreEnvVars.ACME.key
        val value = CoreEnvVars.ACME.value
        if (key != null && value != null) {
            get(key) {
                call.respond(value)
            }
        }
    }
}
