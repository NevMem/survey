package com.nevmem.survey.service.users.data

import com.nevmem.survey.role.RoleEntity

data class UserEntity(
    val id: Long,
    val login: String,
    val name: String,
    val surname: String,
    val email: String,
    val roles: List<RoleEntity>,
)
