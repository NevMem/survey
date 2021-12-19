package com.nevmem.survey.converter

import com.nevmem.survey.data.invite.Invite
import com.nevmem.survey.service.invites.data.InviteEntity
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class InvitesConverter : KoinComponent {
    private val usersConverter by inject<UsersConverter>()

    fun convertInvite(invite: InviteEntity): Invite {
        return Invite(
            inviteId = invite.inviteId,
            acceptedBy = invite.acceptedBy?.let { usersConverter.convertUser(it) },
            isExpired = invite.expired,
        )
    }

    operator fun invoke(invite: InviteEntity) = convertInvite(invite)
}
