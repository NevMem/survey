package com.nevmem.survey.data.response.invite

import com.nevmem.survey.data.invite.Invite
import kotlinx.serialization.Serializable

@Serializable
sealed class CreateInviteResponse {
    @Serializable
    data class CreateInviteError(val message: String) : CreateInviteResponse()

    @Serializable
    data class CreateInviteSuccess(val invite: Invite) : CreateInviteResponse()
}
