package com.nevmem.survey.service.surveys

import com.nevmem.survey.service.surveys.data.CommonQuestionEntity
import com.nevmem.survey.service.surveys.data.QuestionEntity
import com.nevmem.survey.service.surveys.data.SurveyEntity

interface SurveysService {
    suspend fun createSurvey(
        name: String,
        questions: List<QuestionEntity>,
        commonQuestion: List<CommonQuestionEntity>,
    ): SurveyEntity

    suspend fun allSurveys(): List<SurveyEntity>
}