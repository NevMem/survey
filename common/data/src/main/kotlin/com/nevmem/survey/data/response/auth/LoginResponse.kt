package com.nevmem.survey.data.response.auth

import com.nevmem.survey.Exported
import kotlinx.serialization.Serializable

@Serializable
@Exported
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