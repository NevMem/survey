package com.nevmem.survey.invite

import com.nevmem.survey.user.UserEntity

data class InviteEntity(
    val inviteId: String,
    val createdAt: Long,
    val expirationPeriod: Long,
    val owner: UserEntity,
    val acceptedBy: UserEntity?,
    val expired: Boolean,
)
