package com.nevmem.survey.data.response.error

import com.nevmem.survey.Exported
import kotlinx.serialization.Serializable

@Serializable
@Exported
data class ServerError(
    val message: String,
)
