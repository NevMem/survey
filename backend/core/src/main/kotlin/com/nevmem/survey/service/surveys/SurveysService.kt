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

    suspend fun deleteSurvey(id: Long)

    suspend fun allSurveys(): List<SurveyEntity>

    suspend fun activateSurvey(id: Long)

    suspend fun currentActiveSurvey(): SurveyEntity?
}
