package com.nevmem.survey.data.request.task

import com.nevmem.survey.Exported
import kotlinx.serialization.Serializable

@Exported
@Serializable
data class TasksInSurvey(
    val surveyId: Long,
)
