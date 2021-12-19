package com.nevmem.survey.plugins

import com.nevmem.survey.data.response.error.ServerError
import com.nevmem.survey.exception.ForbiddenException
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.StatusPages
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond

fun Application.statusPages() {
    install(StatusPages) {
        exception<ForbiddenException> {
            call.respond(
                HttpStatusCode.Forbidden,
                ServerError(it.message ?: "Unknown error"),
            )
        }

        exception<Throwable> {
            call.respond(
                HttpStatusCode.InternalServerError,
                ServerError(it.message ?: "Unknown error"),
            )
        }
    }
}
