package com.nevmem.survey.data.request.push

import com.nevmem.survey.data.user.UserId
import kotlinx.serialization.Serializable

@Serializable
data class RegisterPushTokenRequest(
    val uid: UserId,
    val token: String?,
)
