package com.nevmem.survey.push.service.push

interface PushService {
    suspend fun broadcastAll(title: String, message: String): Long
}
