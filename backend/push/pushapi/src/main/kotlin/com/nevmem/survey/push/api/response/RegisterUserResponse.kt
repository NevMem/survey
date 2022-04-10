package com.nevmem.survey.push.api.response

import com.nevmem.survey.data.user.UserId
import kotlinx.serialization.Serializable

@Serializable
data class RegisterUserResponse(
    val userId: UserId,
)
