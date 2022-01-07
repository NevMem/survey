package com.nevmem.survey.data.response.auth

import com.nevmem.survey.Exported
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Exported
sealed class RegisterResponse {
    @Serializable
    @SerialName("success")
    data class RegisterSuccessful(
        val token: String,
    ) : RegisterResponse()

    @Serializable
    @SerialName("error")
    data class RegisterError(
        val message: String,
    ) : RegisterResponse()
}
