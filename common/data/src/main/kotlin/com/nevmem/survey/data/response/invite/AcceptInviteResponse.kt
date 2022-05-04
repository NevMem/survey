package com.nevmem.survey.data.response.invite

import com.nevmem.survey.Exported
import kotlinx.serialization.Serializable

@Exported
@Serializable
enum class AcceptInviteStatus {
    Ok,
    Expired,
}

@Exported
@Serializable
data class AcceptInviteResponse(val status: AcceptInviteStatus)
