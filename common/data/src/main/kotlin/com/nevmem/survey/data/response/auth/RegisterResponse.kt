package com.nevmem.survey.data.response.auth

import com.nevmem.survey.Exported
import kotlinx.serialization.Serializable

@Serializable
@Exported
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
