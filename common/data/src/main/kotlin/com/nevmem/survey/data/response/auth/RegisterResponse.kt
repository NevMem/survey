package com.nevmem.survey.data.response.auth

import kotlinx.serialization.Serializable

@Serializable
sealed class RegisterResponse {
    @Serializable
    data class RegisterSuccessful(
        val token: String,
    ) : RegisterResponse()

    @Serializable
    data class RegisterError(
        val message: String,
    ) : RegisterResponse()
}
