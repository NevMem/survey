package com.nevmem.survey.service.invites.data

import com.nevmem.survey.service.users.data.UserEntity

data class InviteEntity(
    val inviteId: String,
    val createdAt: Long,
    val expirationPeriod: Long,
    val owner: UserEntity,
    val acceptedBy: UserEntity?,
    val expired: Boolean,
)
