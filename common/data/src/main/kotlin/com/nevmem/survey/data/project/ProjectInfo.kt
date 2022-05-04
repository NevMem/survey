package com.nevmem.survey.data.project

import com.nevmem.survey.Exported
import com.nevmem.survey.data.role.Role
import com.nevmem.survey.data.user.Administrator
import kotlinx.serialization.Serializable

@Exported
@Serializable
data class ProjectAdministratorInfo(
    val administrator: Administrator,
    val roles: List<Role>,
)

@Exported
@Serializable
data class ProjectInfo(
    val administratorsInfo: List<ProjectAdministratorInfo>,
)
