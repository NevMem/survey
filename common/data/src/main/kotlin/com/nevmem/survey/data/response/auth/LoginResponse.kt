package com.nevmem.survey.data.response.auth

import kotlinx.serialization.Serializable

@Serializable
sealed class LoginResponse {
    @Serializable
    data class LoginSuccessful(
        val token: String,
    ) : LoginResponse()

    @Serializable
    data class LoginError(
        val error: String,
    ) : LoginResponse()
}