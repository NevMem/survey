package com.nevmem.survey.network.internal

import com.nevmem.survey.network.api.NetworkService
import com.nevmem.survey.data.answer.QuestionAnswer
import com.nevmem.survey.data.answer.SurveyAnswer
import com.nevmem.survey.data.request.answer.PublishAnswerRequest
import com.nevmem.survey.data.request.survey.GetSurveyRequest
import com.nevmem.survey.data.response.answer.PublishAnswerResponse
import com.nevmem.survey.data.response.survey.GetSurveyResponse
import com.nevmem.survey.data.survey.Survey
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

internal class NetworkServiceImpl : NetworkService {

    private val baseUrl = "https://ethnosurvey.com"

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
        return client.post<GetSurveyResponse>("$baseUrl/v1/survey/get") {
            contentType(ContentType.Application.Json)
            body = GetSurveyRequest(surveyId)
        }.survey
    }

    override suspend fun sendSurvey(
        surveyId: String,
        publisherId: String,
        answers: List<QuestionAnswer>
    ) {
        val request = PublishAnswerRequest(
            answer = SurveyAnswer(
                publisherId = publisherId,
                surveyId = surveyId,
                answers = answers,
                gallery = null,
            ),
        )
        post<PublishAnswerRequest, PublishAnswerResponse>(
            "$baseUrl/v1/answers/publish",
            request,
        )
    }

    private suspend inline fun <U : Any, reified T : Any> post(url: String, data: U): T {
        return client.post(url) {
            contentType(ContentType.Application.Json)
            body = data
        }
    }
}
