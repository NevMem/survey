package com.nevmem.survey.data.request.invite

import kotlinx.serialization.Serializable

@Serializable
data class CreateInviteRequest(
    val expirationTimeSeconds: Boolean,
)
