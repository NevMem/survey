package com.nevmem.survey.data.project

import com.nevmem.survey.Exported
import com.nevmem.survey.data.user.Administrator
import kotlinx.serialization.Serializable

@Exported
@Serializable
data class Project(
    val id: Long,
    val name: String,
    val owner: Administrator,
)
