package com.nevmem.survey.data.request.project

import com.nevmem.survey.Exported
import kotlinx.serialization.Serializable

@Exported
@Serializable
data class GetProjectInfoRequest(
    val projectId: Long,
)
