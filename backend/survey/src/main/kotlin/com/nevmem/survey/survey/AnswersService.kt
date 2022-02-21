package com.nevmem.survey.survey

import com.nevmem.survey.data.answer.SurveyAnswer

interface AnswersService {
    suspend fun publishAnswer(answer: SurveyAnswer)

    suspend fun getAnswers(surveyId: String): String

    suspend fun answers(surveyId: String): List<SurveyAnswer>

    suspend fun getAnswersCount(surveyId: String): Long
}
