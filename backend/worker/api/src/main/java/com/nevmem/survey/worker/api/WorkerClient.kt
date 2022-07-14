package com.nevmem.survey.worker.api

import com.nevmem.survey.data.task.Task
import com.nevmem.survey.data.user.Administrator
import com.nevmem.survey.util.client.SurveyHttpClient
import com.nevmem.survey.util.client.SurveyHttpClientHandle

@SurveyHttpClient
interface WorkerClient {

    @SurveyHttpClientHandle
    suspend fun tasks(surveyId: String): List<Task>

    @SurveyHttpClientHandle
    suspend fun getTask(user: Administrator, taskId: Long): Task
}