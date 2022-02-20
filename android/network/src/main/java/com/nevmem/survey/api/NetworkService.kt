package com.nevmem.survey.api

import com.nevmem.survey.data.answer.QuestionAnswer
import com.nevmem.survey.data.survey.Survey

interface NetworkService {
    suspend fun loadSurvey(surveyId: String): Survey

    suspend fun sendSurvey(surveyId: String, publisherId: String, answers: List<QuestionAnswer>)
}
