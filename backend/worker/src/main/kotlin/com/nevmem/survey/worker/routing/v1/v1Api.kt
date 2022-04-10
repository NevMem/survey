package com.nevmem.survey.worker.routing.v1

import com.nevmem.survey.worker.routing.v1.tasks.tasks
import io.ktor.routing.Route
import io.ktor.routing.route

fun Route.v1Api() {
    route("/v1") {
        tasks()
    }
}
