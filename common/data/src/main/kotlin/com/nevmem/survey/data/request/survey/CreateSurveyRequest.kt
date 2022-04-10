package com.nevmem.survey.data.request.survey

import com.nevmem.survey.Exported
import com.nevmem.survey.data.question.CommonQuestion
import com.nevmem.survey.data.question.Question
import com.nevmem.survey.data.survey.Survey
import kotlinx.serialization.Serializable

@Serializable
@Exported
data class CreateSurveyRequest(
    val projectId: Long,
    val name: String,
    val questions: List<Question>,
    val commonQuestions: List<CommonQuestion>,
    val answerCoolDown: Long = Survey.SURVEY_COOL_DOWN_ONLY_ONCE,
)
