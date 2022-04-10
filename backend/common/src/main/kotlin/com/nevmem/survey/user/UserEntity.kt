package com.nevmem.survey.user

data class UserEntity(
    val id: Long,
    val login: String,
    val name: String,
    val surname: String,
    val email: String,
)
