package com.nevmem.survey.push.client.internal

import com.nevmem.survey.data.user.UserId
import com.nevmem.survey.push.api.request.BroadcastAllRequest
import com.nevmem.survey.push.api.request.RegisterUserRequest
import com.nevmem.survey.push.api.response.BroadcastAllResponse
import com.nevmem.survey.push.api.response.RegisterUserResponse
import com.nevmem.survey.push.client.PushClient
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.DEFAULT
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.contentType

internal class PushClientImpl(
    private val baseUrl: String,
) : PushClient {
    private val client = HttpClient {
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.BODY
        }
    }

    override suspend fun register(userId: UserId, token: String?) {
        post<RegisterUserRequest, RegisterUserResponse>("/v1/register", RegisterUserRequest(userId, token))
    }

    override suspend fun broadcastAll(title: String, message: String): Long {
        val response = post<BroadcastAllRequest, BroadcastAllResponse>(
            "/v1/broadcast",
            BroadcastAllRequest(title, message),
        )
        return response.sentMessages
    }

    private suspend inline fun <Req : Any, reified Res : Any> post(path: String, body: Req): Res {
        return client.post("$baseUrl$path") {
            this.body = body
            contentType(ContentType.Application.Json)
        }
    }
}
