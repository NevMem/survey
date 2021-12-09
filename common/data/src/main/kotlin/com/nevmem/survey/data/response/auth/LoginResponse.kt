package com.nevmem.survey.data.response.auth

import kotlinx.serialization.Serializable

@Serializable
sealed class LoginResponse {
    data class LoginSuccessful(
        val token: String,
    ) : LoginResponse()

    data class LoginError(
        val error: String,
    ) : LoginResponse()
}