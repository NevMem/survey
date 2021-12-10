package com.nevmem.survey

import com.nevmem.survey.di.di
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.nevmem.survey.plugins.*
import com.nevmem.survey.routing.configureRouting

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        di()
        configureSerialization()
        configureMonitoring()
        configureHTTP()
        configureSecurity()
        configureRouting()
    }.start(wait = true)
}
