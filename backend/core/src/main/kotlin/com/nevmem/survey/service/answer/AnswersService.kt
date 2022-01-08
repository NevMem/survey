package com.nevmem.survey.service.answer

import com.nevmem.survey.data.answer.SurveyAnswer

interface AnswersService {
    suspend fun publishAnswer(answer: SurveyAnswer, publisherId: String)
}
