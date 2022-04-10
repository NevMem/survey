package com.nevmem.survey.worker.api

import com.nevmem.survey.worker.api.internal.WorkerApiImpl

fun createWorkerApi(baseUrl: String): WorkerApi = WorkerApiImpl(baseUrl)
