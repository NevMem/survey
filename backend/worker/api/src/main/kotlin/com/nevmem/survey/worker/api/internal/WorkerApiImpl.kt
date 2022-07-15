package com.nevmem.survey.worker.api.internal

import com.nevmem.survey.data.task.Task
import com.nevmem.survey.data.user.Administrator
import com.nevmem.survey.worker.api.ErrorCreateTask
import com.nevmem.survey.worker.api.GeneratedWorkerClient
import com.nevmem.survey.worker.api.TaskNotFoundException
import com.nevmem.survey.worker.api.WorkerApi
import com.nevmem.survey.worker.api.request.CreateExportDataTaskRequest
import com.nevmem.survey.worker.api.request.GetExportDataTasksBySurveyId
import com.nevmem.survey.worker.api.request.GetTaskRequest
import com.nevmem.survey.worker.api.response.CreateExportDataTaskResponse

internal class WorkerApiImpl(
    baseUrl: String,
) : WorkerApi {

    private val client2 = GeneratedWorkerClient(baseUrl)

    override suspend fun tasks(surveyId: Long): List<Task> {
        return client2.tasks(GetExportDataTasksBySurveyId(surveyId))
    }

    override suspend fun getTask(user: Administrator, taskId: Long): Task {
        val response = client2.getTask(GetTaskRequest(user, taskId))
        return response.task ?: throw TaskNotFoundException(taskId)
    }

    override suspend fun createExportDataTask(user: Administrator, surveyId: Long): Task {
        val response = client2.createExportDataTask(CreateExportDataTaskRequest(user, surveyId))
        return when (response) {
            is CreateExportDataTaskResponse.Success -> response.task
            is CreateExportDataTaskResponse.Error -> throw ErrorCreateTask(response.message)
        }
    }
}
