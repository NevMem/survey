package com.nevmem.survey.worker

import com.nevmem.survey.fs.createFileSystemService
import com.nevmem.survey.media.createMediaStorageService
import com.nevmem.survey.survey.createAnswersService
import com.nevmem.survey.survey.createSurveysService
import com.nevmem.survey.task.createTaskService
import com.nevmem.survey.worker.internal.AvailableTasksProvider
import com.nevmem.survey.worker.internal.Exporter
import com.nevmem.survey.worker.internal.TaskLocker
import com.nevmem.survey.worker.internal.initLogic
import com.nevmem.survey.worker.plugins.installMetrics
import com.nevmem.survey.worker.routing.configureRouting
import com.nevmem.survey.worker.setup.setupDatabases
import com.nevmem.surveys.converters.ExportDataTaskConverter
import com.nevmem.surveys.converters.MediaConverter
import com.nevmem.surveys.converters.TaskLogConverter
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.metrics.micrometer.MicrometerMetrics
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.serialization.json
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics
import io.micrometer.core.instrument.binder.system.ProcessorMetrics
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import org.koin.ktor.ext.Koin
import org.slf4j.event.Level

private val coreModule = module {
    single { createTaskService() }
    single { createFileSystemService() }
    single { createSurveysService() }
    single { createAnswersService() }
    single { createMediaStorageService() }
}

private val convertersModule = module {
    single { ExportDataTaskConverter() }
    single { TaskLogConverter() }
    single { MediaConverter() }
}

private val logicModule = module {
    single { AvailableTasksProvider() }
    single { TaskLocker() }
    single { Exporter() }
}

fun main() {
    setupDatabases()

    embeddedServer(Netty, port = 80, host = "0.0.0.0") {
        install(CallLogging) {
            level = Level.INFO
        }
        install(Koin) {
            modules(
                coreModule,
                convertersModule,
                logicModule,
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
        initLogic()
    }.start(wait = true)
}
