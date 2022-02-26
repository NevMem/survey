package com.nevmem.survey.data.user

import com.nevmem.survey.Exported
import kotlinx.serialization.Serializable

@Exported
@Serializable
data class UserId(
    val uuid: String,
)
