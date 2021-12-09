package com.nevmem.survey.data.request.auth

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val name: String,
    val login: String,
    val password: String,
    val email: String,
)