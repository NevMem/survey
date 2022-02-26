package com.nevmem.survey.data.response.role

import com.nevmem.survey.Exported
import com.nevmem.survey.data.role.Role
import com.nevmem.survey.data.user.Administrator
import kotlinx.serialization.Serializable

@Serializable
@Exported
data class UpdateRolesResponse(
    val administrator: Administrator,
    val roles: List<Role>,
)
