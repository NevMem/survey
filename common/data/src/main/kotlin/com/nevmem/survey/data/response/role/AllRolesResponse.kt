package com.nevmem.survey.data.response.role

import com.nevmem.survey.Exported
import com.nevmem.survey.data.role.Role
import kotlinx.serialization.Serializable

@Serializable
@Exported
data class AllRolesResponse(
    val roles: List<Role>,
)
