package com.nevmem.survey.network.api

interface BackendBaseUrlProvider {
    suspend fun provideBaseUrl(): String
}
