package com.nevmem.survey.routing

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.configureRouting() {
    routing {
        get("/ping") {
            call.respondText("pong")
        }
        
        get("/.well-known/acme-challenge/O8aCFl7hY0YNvS0lAskzzF_s6mMz0qN9CeMd6Uesv4Q") {
            call.respondText("O8aCFl7hY0YNvS0lAskzzF_s6mMz0qN9CeMd6Uesv4Q.5N97G4tmA5CuiRNjIVVaA1ntNMIXvoBWwW72RFKLVBg")
        }
    }
}
