package com.nevmem.survey.routing.v2

import io.ktor.routing.Route
import io.ktor.routing.route

fun Route.v2Api() {
    route("/v2") {
        users()
        surveys()
        projects()
        invites()
    }
}
