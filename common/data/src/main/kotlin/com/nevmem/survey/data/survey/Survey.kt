package com.nevmem.survey.data.survey

import com.nevmem.survey.Exported
import com.nevmem.survey.data.question.CommonQuestion
import com.nevmem.survey.data.question.Question
import kotlinx.serialization.Serializable

@Serializable
@Exported
data class Survey(
    val id: Long,
    val projectId: Long,
    val surveyId: String,
    val name: String,
    val questions: List<Question>,
    val commonQuestions: List<CommonQuestion>,
    val surveyCoolDown: Long = SURVEY_COOL_DOWN_ONLY_ONCE,
) {
    companion object {
        const val SURVEY_COOL_DOWN_ONLY_ONCE = -1L
        const val IGNORE_COOL_DOWN = -2L
    }
}
