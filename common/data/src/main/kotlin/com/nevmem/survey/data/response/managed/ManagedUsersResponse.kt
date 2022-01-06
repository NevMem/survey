package com.nevmem.survey.data.response.managed

import com.nevmem.survey.Exported
import com.nevmem.survey.data.user.User
import kotlinx.serialization.Serializable

@Serializable
@Exported
data class ManagedUsersResponse(
    val users: List<User>,
)
