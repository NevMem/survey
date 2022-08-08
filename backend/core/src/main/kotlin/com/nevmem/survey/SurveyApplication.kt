package com.nevmem.survey

import com.nevmem.survey.db.initializeDatabases
import com.nevmem.survey.di.di
import com.nevmem.survey.plugins.configureHTTP
import com.nevmem.survey.plugins.configureMonitoring
import com.nevmem.survey.plugins.configureSecurity
import com.nevmem.survey.plugins.configureSerialization
import com.nevmem.survey.plugins.logging
import com.nevmem.survey.plugins.statusPages
import com.nevmem.survey.routing.configureRouting
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main() {
    embeddedServer(Netty, port = 80, host = "0.0.0.0") {
        logging()
        statusPages()

        initializeDatabases()
        di()
        configureSerialization()
        configureMonitoring()
        configureHTTP()
        configureSecurity()
        configureRouting()
    }.start(wait = true)
}
