package com.nevmem.surveys.converters

import com.nevmem.survey.data.invite.Invite
import com.nevmem.survey.data.invite.InviteStatus
import com.nevmem.survey.invite.InviteEntity
import com.nevmem.survey.invite.InviteEntityStatus
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class InvitesConverter : KoinComponent {
    private val usersConverter by inject<UsersConverter>()
    private val projectConverter by inject<ProjectConverter>()

    private fun convertInvite(invite: InviteEntity): Invite {
        return Invite(
            id = invite.id,
            project = projectConverter(invite.project),
            toUser = usersConverter(invite.toUser),
            status = invite.status.common,
        )
    }

    operator fun invoke(invite: InviteEntity) = convertInvite(invite)

    private val InviteEntityStatus.common: InviteStatus
        get() = when (this) {
            InviteEntityStatus.Accepted -> InviteStatus.Accepted
            InviteEntityStatus.Waiting -> InviteStatus.Waiting
            InviteEntityStatus.Expired -> InviteStatus.Expired
        }
}
