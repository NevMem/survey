package com.nevmem.survey.cloud.internal

import com.nevmem.survey.cloud.CloudServices
import com.nevmem.survey.cloud.MessagingService
import com.nevmem.survey.env.EnvVars
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
import kotlinx.serialization.Serializable

@Serializable
data class SendMessage(val message: String)

private class MessagingServiceImpl: MessagingService {
    private val baseUrl = "https://functions.yandexcloud.net/"

    private val client by lazy {
        HttpClient {
            install(JsonFeature) { serializer = KotlinxSerializer() }
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.NONE
            }
        }
    }

    override suspend fun sendMessage(message: String) {
        return post("d4e8vjcobk58f3u8ut2p", SendMessage("${EnvVars.environment}: $message"))
    }

    private suspend inline fun <Req : Any, reified Res : Any> post(path: String, body: Req): Res {
        return client.post(baseUrl + path) {
            this.body = body
            contentType(ContentType.Application.Json)
        }
    }
}

internal class CloudServicesImpl: CloudServices {
    override val messaging: MessagingService by lazy {
        MessagingServiceImpl()
    }
}
