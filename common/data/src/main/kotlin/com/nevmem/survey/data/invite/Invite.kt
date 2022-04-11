package com.nevmem.survey.data.invite

import com.nevmem.survey.Exported
import com.nevmem.survey.data.project.Project
import com.nevmem.survey.data.user.Administrator
import kotlinx.serialization.Serializable

@Exported
@Serializable
enum class InviteStatus {
    Accepted,
    Expired,
    Waiting,
}

@Serializable
@Exported
data class Invite(
    val id: Long,
    val project: Project,
    val toUser: Administrator,
    val status: InviteStatus,
)
