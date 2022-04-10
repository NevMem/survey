package com.nevmem.survey.worker.api

import com.nevmem.survey.data.task.Task
import com.nevmem.survey.data.user.Administrator

interface WorkerApi {
    suspend fun tasks(): List<Task>
    suspend fun getTask(user: Administrator, taskId: Long): Task
    suspend fun createExportDataTask(user: Administrator, surveyId: Long): Task
}
