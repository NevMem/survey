package com.nevmem.survey.network.internal

import android.net.Uri
import androidx.core.net.toFile
import com.nevmem.survey.data.answer.QuestionAnswer
import com.nevmem.survey.data.answer.SurveyAnswer
import com.nevmem.survey.data.media.Media
import com.nevmem.survey.data.media.MediaGallery
import com.nevmem.survey.data.request.answer.PublishAnswerRequest
import com.nevmem.survey.data.request.media.CreateGalleryRequest
import com.nevmem.survey.data.request.push.RegisterPushTokenRequest
import com.nevmem.survey.data.request.survey.JoinSurveyRequest
import com.nevmem.survey.data.response.answer.PublishAnswerResponse
import com.nevmem.survey.data.response.media.CreateGalleryResponse
import com.nevmem.survey.data.response.push.RegisterPushTokenResponse
import com.nevmem.survey.data.response.survey.JoinSurveyResponse
import com.nevmem.survey.data.survey.Survey
import com.nevmem.survey.data.user.UserId
import com.nevmem.survey.network.api.BackendBaseUrlProvider
import com.nevmem.survey.network.api.NetworkService
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.DEFAULT
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.util.InternalAPI
import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.writeFully

internal class NetworkServiceImpl(
    private val backendBaseUrlProvider: BackendBaseUrlProvider,
) : NetworkService {

    private val client = HttpClient {
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.BODY
        }
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
    }

    override suspend fun loadSurvey(uid: UserId, surveyId: String): Survey {
        return client.post<JoinSurveyResponse>("${baseUrl()}/v2/survey/join") {
            contentType(ContentType.Application.Json)
            body = JoinSurveyRequest(surveyId, uid)
        }.survey
    }

    override suspend fun sendSurvey(
        surveyId: String,
        uid: UserId,
        answers: List<QuestionAnswer>,
        mediaGallery: MediaGallery?,
    ) {
        val request = PublishAnswerRequest(
            answer = SurveyAnswer(
                uid = uid,
                timestamp = System.currentTimeMillis(),
                surveyId = surveyId,
                answers = answers,
                gallery = mediaGallery,
            ),
        )
        post<PublishAnswerRequest, PublishAnswerResponse>(
            "${baseUrl()}/v1/answers/publish",
            request,
        )
    }

    override suspend fun sendToken(uid: UserId, token: String?) {
        val request = RegisterPushTokenRequest(
            uid = uid,
            token = token,
        )
        post<RegisterPushTokenRequest, RegisterPushTokenResponse>(
            "${baseUrl()}/v1/push/register",
            request,
        )
    }

    override suspend fun createGallery(medias: List<Media>): MediaGallery {
        return this.post<CreateGalleryRequest, CreateGalleryResponse>(
            "${baseUrl()}/v1/media/create_gallery",
            CreateGalleryRequest(medias),
        ).gallery
    }

    @OptIn(InternalAPI::class)
    override suspend fun sendMedia(media: Uri): Media {
        val file = media.toFile()
        return client.post<Media>("${baseUrl()}/v1/media/upload") {
            body = MultiPartFormDataContent(
                formData {
                    appendInput(
                        "file",
                        headers = Headers.build {
                            append(HttpHeaders.ContentDisposition, "filename=${file.name}")
                        },
                        size = file.length(),
                    ) {
                        buildPacket {
                            writeFully(file.readBytes())
                        }
                    }
                }
            )
        }
    }

    private suspend fun baseUrl(): String = backendBaseUrlProvider.provideBaseUrl()

    private suspend inline fun <U : Any, reified T : Any> post(url: String, data: U): T {
        return client.post(url) {
            contentType(ContentType.Application.Json)
            body = data
        }
    }
}
