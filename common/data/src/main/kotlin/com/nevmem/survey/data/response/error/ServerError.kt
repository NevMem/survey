package com.nevmem.survey.data.response.error

import kotlinx.serialization.Serializable

@Serializable
data class ServerError(
    val message: String,
)
