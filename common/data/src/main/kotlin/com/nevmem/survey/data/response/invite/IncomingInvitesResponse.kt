package com.nevmem.survey.data.response.invite

import com.nevmem.survey.Exported
import com.nevmem.survey.data.invite.Invite
import kotlinx.serialization.Serializable

@Exported
@Serializable
data class IncomingInvitesResponse(
    val invites: List<Invite>,
)
