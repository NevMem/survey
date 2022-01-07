package com.nevmem.survey.data.response.invite

import com.nevmem.survey.Exported
import com.nevmem.survey.data.invite.Invite
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Exported
sealed class CreateInviteResponse {
    @Serializable
    @SerialName("error")
    data class CreateInviteError(val message: String) : CreateInviteResponse()

    @Serializable
    @SerialName("success")
    data class CreateInviteSuccess(val invite: Invite) : CreateInviteResponse()
}
