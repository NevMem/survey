package com.nevmem.survey.cloud

import com.nevmem.survey.cloud.internal.CloudServicesImpl

interface MessagingService {
    suspend fun sendMessage(message: String)
}

interface CloudServices {
    val messaging: MessagingService

    companion object Factory {
        fun create(): CloudServices = CloudServicesImpl()
    }
}
