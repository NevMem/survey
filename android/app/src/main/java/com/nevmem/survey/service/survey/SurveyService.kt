package com.nevmem.survey.service.survey

import com.nevmem.survey.api.NetworkService
import com.nevmem.survey.data.answer.QuestionAnswer
import com.nevmem.survey.data.survey.Survey
import com.nevmem.survey.service.preferences.PreferencesService
import com.nevmem.survey.service.publisher.PublisherIdProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.serialization.json.Json

class SurveyService(
    private val preferencesService: PreferencesService,
    private val networkService: NetworkService,
    private val publisherIdProvider: PublisherIdProvider,
) {
    private var currentSurvey: Survey? = null
        set(value) {
            field = value
            while (!surveyFlow.tryEmit(field)) {}
        }

    private val surveyFlow = MutableSharedFlow<Survey?>(replay = 1)
    val surveys: Flow<Survey?> = surveyFlow
    val survey: Survey
        get() = currentSurvey!!

    init {
        val prefValue = preferencesService.get("currentSurvey")
        currentSurvey = if (prefValue != null) {
            try {
                Json.decodeFromString(Survey.serializer(), prefValue)
            } catch (exception: Exception) {
                null
            }
        } else {
            null
        }
    }

    fun leaveSurvey() {
        currentSurvey = null
        preferencesService.delete("currentSurvey")
    }

    fun saveSurvey(survey: Survey) {
        currentSurvey = survey
        preferencesService.put("currentSurvey", Json.encodeToString(Survey.serializer(), survey))
    }

    suspend fun sendAnswer(answers: List<QuestionAnswer>) {
        networkService.sendSurvey(survey.surveyId, publisherIdProvider.provide(), answers)
    }
}
