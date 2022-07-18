package com.nevmem.survey.util.client

enum class ClientLogLevel {
    All,
    Headers,
    Body,
    Info,
    None,
}

@Target(AnnotationTarget.CLASS)
annotation class SurveyHttpClient(val logLevel: ClientLogLevel = ClientLogLevel.None)
