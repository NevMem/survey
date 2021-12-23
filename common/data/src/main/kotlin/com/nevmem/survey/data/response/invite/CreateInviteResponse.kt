package com.nevmem.survey.data.response.invite

import com.nevmem.survey.Exported
import com.nevmem.survey.data.invite.Invite
import kotlinx.serialization.Serializable

@Serializable
@Exported
sealed class CreateInviteResponse {
    @Serializable
    data class CreateInviteError(val message: String) : CreateInviteResponse()

    @Serializable
    data class CreateInviteSuccess(val invite: Invite) : CreateInviteResponse()
}
