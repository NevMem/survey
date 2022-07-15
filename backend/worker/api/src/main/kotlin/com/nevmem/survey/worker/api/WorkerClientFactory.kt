package com.nevmem.survey.worker.api

object WorkerClientFactory {
    fun create(baseUrl: String): WorkerClient = GeneratedWorkerClient(baseUrl)
}