package com.nevmem.survey.data.response.project

import com.nevmem.survey.Exported
import com.nevmem.survey.data.project.ProjectInfo
import kotlinx.serialization.Serializable

@Exported
@Serializable
data class GetProjectInfoResponse(
    val projectInfo: ProjectInfo,
)
