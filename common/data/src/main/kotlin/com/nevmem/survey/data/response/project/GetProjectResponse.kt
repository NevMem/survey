package com.nevmem.survey.data.response.project

import com.nevmem.survey.Exported
import com.nevmem.survey.data.project.Project
import kotlinx.serialization.Serializable

@Exported
@Serializable
data class GetProjectResponse(
    val project: Project,
)
