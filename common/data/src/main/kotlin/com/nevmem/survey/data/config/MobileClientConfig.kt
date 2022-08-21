package com.nevmem.survey.data.config

import kotlinx.serialization.Serializable

@Serializable
data class MobileClientConfig(
    val pushNotificationsEnabled: Boolean,
) {
    companion object {
        val default: MobileClientConfig = MobileClientConfig(
            false,
        )
    }
}