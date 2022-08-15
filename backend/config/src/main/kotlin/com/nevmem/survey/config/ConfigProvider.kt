package com.nevmem.survey.config

import com.nevmem.survey.cloud.MessagingService
import com.nevmem.survey.config.internal.ConfigProviderImpl
import com.nevmem.survey.s3client.S3ClientWrapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.Serializable

@Serializable
data class Config(
    val version: Long,
)

interface ConfigProvider {
    val config: Flow<Config>

    companion object Factory {
        fun create(
            scope: CoroutineScope,
            s3ClientWrapper: S3ClientWrapper,
            messagingService: MessagingService,
        ): ConfigProvider = ConfigProviderImpl(scope, s3ClientWrapper, messagingService)
    }
}
