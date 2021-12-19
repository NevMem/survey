package com.nevmem.survey.plugins

import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.CallLogging
import org.slf4j.event.Level


fun Application.logging() {
    install(CallLogging) {
        level = Level.INFO
    }
}
