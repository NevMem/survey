package com.nevmem.survey.push.client

import com.nevmem.survey.push.api.request.BroadcastAllRequest
import com.nevmem.survey.push.api.request.RegisterUserRequest
import com.nevmem.survey.push.api.response.BroadcastAllResponse
import com.nevmem.survey.push.api.response.RegisterUserResponse
import com.nevmem.survey.util.client.RetryPolicy
import com.nevmem.survey.util.client.SurveyHttpClient
import com.nevmem.survey.util.client.SurveyHttpClientHandle

@SurveyHttpClient
interface PushClient {
    @SurveyHttpClientHandle("/v1/register", retryPolicy = RetryPolicy.ExponentialFinite)
    suspend fun register(request: RegisterUserRequest): RegisterUserResponse

    @SurveyHttpClientHandle("/v1/broadcast")
    suspend fun broadcastAll(request: BroadcastAllRequest): BroadcastAllResponse
}
