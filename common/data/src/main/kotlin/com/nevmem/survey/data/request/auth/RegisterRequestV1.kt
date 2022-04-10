package com.nevmem.survey.data.request.auth

import com.nevmem.survey.Exported
import kotlinx.serialization.Serializable

@Serializable
@Exported
data class RegisterRequestV1(
    val name: String,
    val surname: String,
    val login: String,
    val password: String,
    val email: String,
    val inviteId: String,
)
