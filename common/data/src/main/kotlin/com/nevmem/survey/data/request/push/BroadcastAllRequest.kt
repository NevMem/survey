package com.nevmem.survey.data.request.push

import com.nevmem.survey.Exported
import kotlinx.serialization.Serializable

@Exported
@Serializable
data class BroadcastAllRequest(
    val title: String,
    val message: String,
)
