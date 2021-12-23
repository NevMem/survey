package com.nevmem.survey.data.request.invite

import com.nevmem.survey.Exported
import kotlinx.serialization.Serializable

@Serializable
@Exported
data class CreateInviteRequest(
    val expirationTimeSeconds: Long,
)
