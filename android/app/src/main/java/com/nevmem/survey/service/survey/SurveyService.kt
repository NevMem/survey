package com.nevmem.survey.service.survey

import com.nevmem.survey.data.survey.Survey
import com.nevmem.survey.service.preferences.PreferencesService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.serialization.json.Json

class SurveyService(
    private val preferencesService: PreferencesService,
) {
    private var currentSurvey: Survey? = null
        set(value) {
            field = value
            while (!surveyFlow.tryEmit(field)) {}
        }

    val hasSurvey: Boolean
        get() = currentSurvey != null

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

    fun saveSurvey(survey: Survey) {
        currentSurvey = survey
        preferencesService.put("currentSurvey", Json.encodeToString(Survey.serializer(), survey))
    }
}
