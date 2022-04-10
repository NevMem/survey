package com.nevmem.survey.push.routing.v1

import io.ktor.routing.Route
import io.ktor.routing.route

fun Route.v1Api() {
    route("/v1") {
        pushes()
    }
}
