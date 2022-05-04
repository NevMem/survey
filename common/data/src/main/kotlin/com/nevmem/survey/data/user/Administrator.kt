package com.nevmem.survey.data.user

import com.nevmem.survey.Exported
import kotlinx.serialization.Serializable

@Serializable
@Exported
data class Administrator(
    val id: Long,
    val login: String,
    val name: String,
    val surname: String,
    val email: String,
)
