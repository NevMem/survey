package com.nevmem.survey.data.request.auth

import com.nevmem.survey.Exported
import kotlinx.serialization.Serializable

@Serializable
@Exported
data class RegisterRequest(
    val name: String,
    val surname: String,
    val login: String,
    val password: String,
    val email: String,
)
