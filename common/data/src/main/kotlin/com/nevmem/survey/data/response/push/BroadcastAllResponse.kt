package com.nevmem.survey.data.response.push

import com.nevmem.survey.Exported
import kotlinx.serialization.Serializable

@Exported
@Serializable
data class BroadcastAllResponse(
    val sentMessages: Long,
)
