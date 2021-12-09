package com.nevmem.survey.data.user

import com.nevmem.survey.data.role.Role
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Long,
    val name: String,
    val surname: String,
    val email: String,
    val roles: List<Role>,
)
