package com.nevmem.survey.data.response.push

import kotlinx.serialization.Serializable

@Serializable
data class RegisterPushTokenResponse(
    val token: String,
)
