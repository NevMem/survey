package com.nevmem.survey.service.invites.internal

import com.nevmem.survey.service.invites.InvitesService
import com.nevmem.survey.service.invites.data.InviteEntity
import com.nevmem.survey.service.users.data.UserEntity

internal class InvitesServiceImpl : InvitesService {
    override suspend fun createInvite(owner: UserEntity, expirationSeconds: Boolean): InviteEntity {
        TODO("Not yet implemented")
    }

    override suspend fun getInviteById(inviteId: String): InviteEntity? {
        TODO("Not yet implemented")
    }

    override suspend fun acceptedBy(inviteEntity: InviteEntity, user: UserEntity) {
        TODO("Not yet implemented")
    }
}