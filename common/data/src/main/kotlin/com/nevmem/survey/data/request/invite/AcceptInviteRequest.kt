package com.nevmem.survey.data.request.invite

import com.nevmem.survey.Exported
import kotlinx.serialization.Serializable

@Exported
@Serializable
data class AcceptInviteRequest(
    val id: Long,
)
