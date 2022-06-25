package com.nevmem.survey.plugins

import com.nevmem.survey.data.response.error.ServerError
import com.nevmem.survey.exception.AccessDeniedException
import com.nevmem.survey.exception.ForbiddenException
import com.nevmem.survey.exception.NotFoundException
import io.ktor.application.Application
import io.ktor.application.application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.StatusPages
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.util.error

private const val UNKNOWN_ERROR = "Unknown error"

fun Application.statusPages() {
    install(StatusPages) {
        exception<AccessDeniedException> {
            call.respond(
                HttpStatusCode.Forbidden,
                ServerError(it.message ?: UNKNOWN_ERROR),
            )
            application.environment.log.error(it)
        }

        exception<ForbiddenException> {
            call.respond(
                HttpStatusCode.Forbidden,
                ServerError(it.message ?: UNKNOWN_ERROR),
            )
            application.environment.log.error(it)
        }

        exception<NotFoundException> {
            call.respond(
                HttpStatusCode.NotFound,
                ServerError("Resource not found")
            )
            application.environment.log.error(it)
        }

        exception<Throwable> {
            call.respond(
                HttpStatusCode.InternalServerError,
                ServerError(it.message ?: UNKNOWN_ERROR),
            )
            application.environment.log.error(it)
        }
    }
}
