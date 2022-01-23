package com.nevmem.survey.invites

import com.nevmem.survey.invite.InviteEntity
import com.nevmem.survey.user.UserEntity

interface InvitesService {
    suspend fun createInvite(owner: UserEntity, expirationSeconds: Long): InviteEntity
    suspend fun getInviteById(inviteId: String): InviteEntity?
    suspend fun acceptedBy(inviteEntity: InviteEntity, user: UserEntity)
    suspend fun userInvites(ownerId: Long): List<InviteEntity>
}