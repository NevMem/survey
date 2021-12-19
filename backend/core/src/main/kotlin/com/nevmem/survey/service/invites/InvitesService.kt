package com.nevmem.survey.service.invites

import com.nevmem.survey.service.invites.data.InviteEntity
import com.nevmem.survey.service.users.data.UserEntity

interface InvitesService {
    suspend fun createInvite(owner: UserEntity, expirationSeconds: Long): InviteEntity
    suspend fun getInviteById(inviteId: String): InviteEntity?
    suspend fun acceptedBy(inviteEntity: InviteEntity, user: UserEntity)
    suspend fun userInvites(ownerId: Long): List<InviteEntity>
}
