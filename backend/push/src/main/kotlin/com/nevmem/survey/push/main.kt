package com.nevmem.survey.push

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.nevmem.survey.env.EnvVars
import com.nevmem.survey.push.plugins.installMetrics
import com.nevmem.survey.push.routing.configureRouting
import com.nevmem.survey.push.service.data.createPushDataService
import com.nevmem.survey.push.service.data.internal.PushDataTable
import com.nevmem.survey.push.service.push.createPushService
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.serialization.json
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.dsl.module
import org.koin.ktor.ext.Koin
import org.slf4j.LoggerFactory
import org.slf4j.event.Level
import java.io.FileInputStream
import java.util.Properties

private val coreModule = module {
    single { createPushDataService() }
    single { createPushService(get()) }
}

private fun setupDatabases() {
    Database.connect(
        HikariDataSource(
            HikariConfig(
                Properties().apply {
                    put("dataSource.user", EnvVars.DataSource.user)
                    put("dataSource.password", EnvVars.DataSource.password)
                    put("dataSource.databaseName", EnvVars.DataSource.databaseName)
                    put("dataSource.serverName", EnvVars.DataSource.serverName)
                    put("dataSource.portNumber", EnvVars.DataSource.portNumber)
                    put("dataSourceClassName", "org.postgresql.ds.PGSimpleDataSource")
                }
            )
        )
    )
    createTables()
    LoggerFactory.getLogger("setup-databases").info("Database initialized")
}

private fun createTables() = transaction {
    SchemaUtils.create(
        PushDataTable,
    )
}

fun main() {
    setupDatabases()

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
