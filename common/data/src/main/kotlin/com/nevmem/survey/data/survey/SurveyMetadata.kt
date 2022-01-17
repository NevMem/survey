package com.nevmem.survey.data.survey

import com.nevmem.survey.Exported
import kotlinx.serialization.Serializable

@Exported
@Serializable
data class SurveyMetadata(
    val answersCount: Long,
    val filesCount: Int,
)
