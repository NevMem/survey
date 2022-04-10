package com.nevmem.survey.survey

import com.nevmem.survey.commonQuestion.CommonQuestionEntity
import com.nevmem.survey.question.QuestionEntity

data class SurveyEntity(
    val id: Long,
    val projectId: Long,
    val surveyId: String,
    val name: String,
    val questions: List<QuestionEntity>,
    val commonQuestions: List<CommonQuestionEntity>,
    val answerCoolDown: Long,
)
