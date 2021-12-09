package com.nevmem.survey.data.response.auth

import kotlinx.serialization.Serializable

@Serializable
sealed class RegisterResponse {
    data class RegisterSuccessful(
        val token: String,
    ) : RegisterResponse()

    data class RegisterError(
        val message: String,
    ) : RegisterResponse()
}
