package com.nevmem.survey.push

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.nevmem.survey.push.plugins.installMetrics
import com.nevmem.survey.push.routing.configureRouting
import com.nevmem.survey.push.service.data.createPushDataService
import com.nevmem.survey.push.service.push.createPushService
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
import java.io.FileInputStream

private val coreModule = module {
    single { createPushDataService() }
    single { createPushService(get()) }
}

fun main() {
    val serviceAccount = FileInputStream("firebase_sdk_key.json")

    val options = FirebaseOptions.builder()
        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
        .build()

    FirebaseApp.initializeApp(options)

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
