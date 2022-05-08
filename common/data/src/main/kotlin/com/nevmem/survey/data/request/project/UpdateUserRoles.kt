package com.nevmem.survey.data.request.project

import com.nevmem.survey.Exported
import com.nevmem.survey.data.role.Role
import com.nevmem.survey.data.user.Administrator
import kotlinx.serialization.Serializable

@Exported
@Serializable
data class UpdateUserRoles(
    val projectId: Long,
    val user: Administrator,
    val roles: List<Role>,
)
