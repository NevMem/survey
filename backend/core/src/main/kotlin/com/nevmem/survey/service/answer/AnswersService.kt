package com.nevmem.survey.service.answer

import com.nevmem.survey.data.answer.SurveyAnswer

interface AnswersService {
    suspend fun publishAnswer(answer: SurveyAnswer, publisherId: String)

    suspend fun getAnswers(surveyId: String): String

    suspend fun getAnswersCount(surveyId: String): Long
}
