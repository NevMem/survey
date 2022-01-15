package com.nevmem.survey.data.answer

import com.nevmem.survey.Exported
import kotlinx.serialization.Serializable

@Serializable
@Exported
data class SurveyAnswer(
    val surveyId: String,
    val answers: List<QuestionAnswer>,
)
