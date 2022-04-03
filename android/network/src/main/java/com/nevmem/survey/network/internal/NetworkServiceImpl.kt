package com.nevmem.survey.network.internal

import com.nevmem.survey.data.answer.QuestionAnswer
import com.nevmem.survey.data.answer.SurveyAnswer
import com.nevmem.survey.data.request.answer.PublishAnswerRequest
import com.nevmem.survey.data.request.push.RegisterPushTokenRegisterRequest
import com.nevmem.survey.data.request.survey.GetSurveyRequest
import com.nevmem.survey.data.response.answer.PublishAnswerResponse
import com.nevmem.survey.data.response.push.RegisterPushTokenResponse
import com.nevmem.survey.data.response.survey.GetSurveyResponse
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
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.contentType

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

    override suspend fun loadSurvey(surveyId: String): Survey {
        return client.post<GetSurveyResponse>("${baseUrl()}/v1/survey/get") {
            contentType(ContentType.Application.Json)
            body = GetSurveyRequest(surveyId)
        }.survey
    }

    override suspend fun sendSurvey(
        surveyId: String,
        uid: UserId,
        answers: List<QuestionAnswer>
    ) {
        val request = PublishAnswerRequest(
            answer = SurveyAnswer(
                uid = uid,
                surveyId = surveyId,
                answers = answers,
                gallery = null,
            ),
        )
        post<PublishAnswerRequest, PublishAnswerResponse>(
            "${baseUrl()}/v1/answers/publish",
            request,
        )
    }

    override suspend fun sendToken(uid: UserId, token: String?) {
        val request = RegisterPushTokenRegisterRequest(
            uid = uid,
            token = token,
        )
        post<RegisterPushTokenRegisterRequest, RegisterPushTokenResponse>(
            "${baseUrl()}/v1/push/register",
            request,
        )
    }

    private suspend fun baseUrl(): String = backendBaseUrlProvider.provideBaseUrl()

    private suspend inline fun <U : Any, reified T : Any> post(url: String, data: U): T {
        return client.post(url) {
            contentType(ContentType.Application.Json)
            body = data
        }
    }
}
