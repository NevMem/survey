package com.nevmem.survey.service.surveys

import com.nevmem.survey.commonQuestion.CommonQuestionEntity
import com.nevmem.survey.question.QuestionEntity
import com.nevmem.survey.survey.SurveyEntity

interface SurveysService {
    suspend fun createSurvey(
        name: String,
        questions: List<QuestionEntity>,
        commonQuestion: List<CommonQuestionEntity>,
    ): SurveyEntity

    suspend fun deleteSurvey(id: Long)

    suspend fun allSurveys(): List<SurveyEntity>
}
