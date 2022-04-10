package com.nevmem.survey.push.api.response

import kotlinx.serialization.Serializable

@Serializable
data class BroadcastAllResponse(
    val sentMessages: Long,
)
