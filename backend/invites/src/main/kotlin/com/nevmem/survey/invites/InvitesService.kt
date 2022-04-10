package com.nevmem.survey.invites

import com.nevmem.survey.invite.InviteEntity
import com.nevmem.survey.project.ProjectEntity
import com.nevmem.survey.user.UserEntity

interface InvitesService {
    suspend fun createInvite(fromUser: UserEntity, toUser: UserEntity, project: ProjectEntity, expirationSeconds: Long): InviteEntity
    suspend fun get(id: Long): InviteEntity?
    suspend fun accept(inviteEntity: InviteEntity)
    suspend fun incomingInvites(user: UserEntity): List<InviteEntity>
    suspend fun outgoingInvites(user: UserEntity): List<InviteEntity>
}
