package com.nevmem.survey.data.user

import com.nevmem.survey.Exported
import com.nevmem.survey.data.role.Role
import kotlinx.serialization.Serializable

@Serializable
@Exported
data class Administrator(
    val id: Long,
    val login: String,
    val name: String,
    val surname: String,
    val email: String,
    val roles: List<Role>,
    val allAvailableRoles: List<Role>,
)
