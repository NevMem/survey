package com.nevmem.survey.worker.api.internal

import com.nevmem.survey.data.task.Task
import com.nevmem.survey.data.user.Administrator
import com.nevmem.survey.worker.api.CreateExportDataTask
import com.nevmem.survey.worker.api.GetTaskRequest
import com.nevmem.survey.worker.api.WorkerApi
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.contentType

internal class WorkerApiImpl(
    private val baseUrl: String,
) : WorkerApi {

    private val client = HttpClient {
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
    }

    override suspend fun tasks(): List<Task> {
        return post("/v1/tasks", Unit)
    }

    override suspend fun getTask(user: Administrator, taskId: Long): Task {
        return post("/v1/task", GetTaskRequest(user, taskId))
    }

    override suspend fun createExportDataTask(user: Administrator, surveyId: Long): Task {
        return post("/v1/create_export_data_task", CreateExportDataTask(user, surveyId))
    }

    private suspend inline fun<Req : Any, reified Res : Any> post(path: String, body: Req): Res {
        return client.post("$baseUrl$path") {
            this.body = body
            contentType(ContentType.Application.Json)
        }
    }
}
