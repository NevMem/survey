package com.nevmem.survey.routing.apiver

import io.ktor.routing.Route
import io.ktor.routing.route

fun Route.v1(builder: Route.() -> Unit) {
    route("/v1") {
        builder(this)
    }
}
