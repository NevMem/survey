package com.nevmem.survey.push

import com.nevmem.survey.push.plugins.installMetrics
import com.nevmem.survey.push.routing.configureRouting
import com.nevmem.survey.push.service.createPushDataService
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.serialization.json
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import org.koin.ktor.ext.Koin
import org.slf4j.event.Level

private val coreModule = module {
    single { createPushDataService() }
}

fun main() {
    embeddedServer(Netty, port = 80, host = "0.0.0.0") {
        install(CallLogging) {
            level = Level.INFO
        }
        install(Koin) {
            modules(
                coreModule,
            )
        }
        install(ContentNegotiation) {
            json(
                Json {
                    useArrayPolymorphism = false
                }
            )
        }
        installMetrics()
        configureRouting()
    }.start(wait = true)
}
