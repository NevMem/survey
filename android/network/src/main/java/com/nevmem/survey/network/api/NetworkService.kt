package com.nevmem.survey.network.api

import com.nevmem.survey.data.answer.QuestionAnswer
import com.nevmem.survey.data.survey.Survey
import com.nevmem.survey.data.user.UserId

interface NetworkService {
    suspend fun loadSurvey(surveyId: String): Survey

    suspend fun sendSurvey(surveyId: String, uid: UserId, answers: List<QuestionAnswer>)

    suspend fun sendToken(uid: UserId, token: String?)
}
