package com.nevmem.survey.data.response.managed

import com.nevmem.survey.Exported
import com.nevmem.survey.data.user.Administrator
import kotlinx.serialization.Serializable

@Serializable
@Exported
data class ManagedUsersResponse(
    val administrators: List<Administrator>,
)
