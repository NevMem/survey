package com.nevmem.survey.invite

import com.nevmem.survey.project.ProjectEntity
import com.nevmem.survey.user.UserEntity

enum class InviteEntityStatus {
    Accepted,
    Expired,
    Waiting,
}

data class InviteEntity(
    val id: Long,
    val project: ProjectEntity,
    val createdAt: Long,
    val expirationPeriod: Long,
    val fromUser: UserEntity,
    val toUser: UserEntity,
    val status: InviteEntityStatus,
)
