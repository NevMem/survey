package com.nevmem.survey.push.api.request

import com.nevmem.survey.data.user.UserId
import kotlinx.serialization.Serializable

@Serializable
data class RegisterUserRequest(
    val userId: UserId,
    val token: String?,
)
