package com.nevmem.survey.data.invite

import com.nevmem.survey.Exported
import com.nevmem.survey.data.user.Administrator
import kotlinx.serialization.Serializable

@Serializable
@Exported
data class Invite(
    val inviteId: String,
    val acceptedBy: Administrator?,
    val isExpired: Boolean,
)
