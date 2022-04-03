package com.nevmem.survey.push.api.request

import kotlinx.serialization.Serializable

@Serializable
data class BroadcastAllRequest(
    val title: String,
    val message: String,
)
