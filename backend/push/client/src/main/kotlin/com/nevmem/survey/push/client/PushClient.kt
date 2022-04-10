package com.nevmem.survey.push.client

import com.nevmem.survey.data.user.UserId

interface PushClient {
    suspend fun register(userId: UserId, token: String?)

    suspend fun broadcastAll(title: String, message: String): Long
}
