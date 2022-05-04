package com.nevmem.survey.survey

import com.nevmem.survey.commonQuestion.CommonQuestionEntity
import com.nevmem.survey.question.QuestionEntity

interface SurveysService {
    suspend fun createSurvey(
        projectId: Long,
        name: String,
        questions: List<QuestionEntity>,
        commonQuestion: List<CommonQuestionEntity>,
        answerCoolDown: Long,
    ): SurveyEntity

    suspend fun surveysInProject(projectId: Long): List<SurveyEntity>

    suspend fun deleteSurvey(id: Long)

    suspend fun survey(surveyId: String): SurveyEntity?

    suspend fun surveyById(surveyId: Long): SurveyEntity?
}
