package com.nevmem.survey.survey

import com.nevmem.survey.data.answer.SurveyAnswer
import kotlinx.coroutines.flow.Flow

interface AnswersService {
    suspend fun publishAnswer(answer: SurveyAnswer)

    suspend fun answers(surveyId: String): List<SurveyAnswer>
    suspend fun answersStreamer(surveyId: String): Flow<SurveyAnswer>

    suspend fun getAnswersCount(surveyId: String): Long
}
