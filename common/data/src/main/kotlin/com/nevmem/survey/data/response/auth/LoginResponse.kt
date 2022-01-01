package com.nevmem.survey.data.response.auth

import com.nevmem.survey.Exported
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Exported
sealed class LoginResponse {
    @Serializable
    @SerialName("success")
    data class LoginSuccessful(
        val token: String,
    ) : LoginResponse()

    @Serializable
    @SerialName("error")
    data class LoginError(
        val error: String,
    ) : LoginResponse()
}