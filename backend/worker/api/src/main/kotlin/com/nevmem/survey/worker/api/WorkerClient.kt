package com.nevmem.survey.worker.api

import com.nevmem.survey.data.task.Task
import com.nevmem.survey.util.client.ClientLogLevel
import com.nevmem.survey.util.client.RetryPolicy
import com.nevmem.survey.util.client.SurveyHttpClient
import com.nevmem.survey.util.client.SurveyHttpClientHandle
import com.nevmem.survey.worker.api.request.CreateExportDataTaskRequest
import com.nevmem.survey.worker.api.request.GetExportDataTasksBySurveyId
import com.nevmem.survey.worker.api.request.GetTaskRequest
import com.nevmem.survey.worker.api.response.CreateExportDataTaskResponse
import com.nevmem.survey.worker.api.response.GetTaskResponse

@SurveyHttpClient(logLevel = ClientLogLevel.All)
interface WorkerClient {

    @SurveyHttpClientHandle(path = "/v1/export_data_tasks_by_survey_id", retryPolicy = RetryPolicy.ExponentialFinite)
    suspend fun tasks(request: GetExportDataTasksBySurveyId): List<Task>

    @SurveyHttpClientHandle(path = "/v1/task", retryPolicy = RetryPolicy.ExponentialFinite)
    suspend fun getTask(request: GetTaskRequest): GetTaskResponse

    @SurveyHttpClientHandle(path = "/v1/create_export_data_task")
    suspend fun createExportDataTask(request: CreateExportDataTaskRequest): CreateExportDataTaskResponse
}
