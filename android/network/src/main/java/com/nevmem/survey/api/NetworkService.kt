package com.nevmem.survey.api

import com.nevmem.survey.data.survey.Survey

interface NetworkService {
    suspend fun loadSurvey(surveyId: String): Survey
}
