package com.nevmem.survey.data.request.auth

import com.nevmem.survey.Exported
import kotlinx.serialization.Serializable

@Serializable
@Exported
data class LoginRequest(
    val login: String,
    val password: String,
)
