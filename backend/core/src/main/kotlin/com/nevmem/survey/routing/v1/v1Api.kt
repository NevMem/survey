package com.nevmem.survey.routing.v1

import com.nevmem.survey.routing.apiver.v1
import com.nevmem.survey.routing.v1.invites.invites
import com.nevmem.survey.routing.v1.surveys.surveys
import io.ktor.routing.Route

fun Route.v1Api() {
    v1 {
        users()
        invites()
        surveys()
    }
}
