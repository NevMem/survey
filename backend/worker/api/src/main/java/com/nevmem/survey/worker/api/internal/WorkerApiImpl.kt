package com.nevmem.survey.worker.api.internal

import com.nevmem.survey.data.task.Task
import com.nevmem.survey.data.user.Administrator
import com.nevmem.survey.worker.api.ErrorCreateTask
import com.nevmem.survey.worker.api.TaskNotFoundException
import com.nevmem.survey.worker.api.request.CreateExportDataTaskRequest
import com.nevmem.survey.worker.api.request.GetTaskRequest
import com.nevmem.survey.worker.api.WorkerApi
import com.nevmem.survey.worker.api.response.CreateExportDataTaskResponse
import com.nevmem.survey.worker.api.response.GetTaskResponse
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
        val response = post<GetTaskRequest, GetTaskResponse>("/v1/task", GetTaskRequest(user, taskId))
        return response.task ?: throw TaskNotFoundException(taskId)
    }

    override suspend fun createExportDataTask(user: Administrator, surveyId: Long): Task {
        val response = post<CreateExportDataTaskRequest, CreateExportDataTaskResponse>(
            "/v1/create_export_data_task",
            CreateExportDataTaskRequest(user, surveyId)
        )
        return when (response) {
            is CreateExportDataTaskResponse.Success -> response.task
            is CreateExportDataTaskResponse.Error -> throw ErrorCreateTask(response.message)
        }
    }

    private suspend inline fun<Req : Any, reified Res : Any> post(path: String, body: Req): Res {
        return client.post("$baseUrl$path") {
            this.body = body
            contentType(ContentType.Application.Json)
        }
    }
}
