package com.nevmem.survey.data.response.invite

import com.nevmem.survey.Exported
import com.nevmem.survey.data.invite.Invite
import kotlinx.serialization.Serializable

@Serializable
@Exported
data class GetInvitesResponse(
    val invites: List<Invite>,
)
