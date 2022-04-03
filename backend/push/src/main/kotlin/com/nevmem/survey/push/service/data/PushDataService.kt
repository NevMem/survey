package com.nevmem.survey.push.service.data

import com.nevmem.survey.data.user.UserId

interface PushDataService {
    suspend fun register(userId: UserId, token: String?)

    suspend fun getAllTokens(): List<String>
}
