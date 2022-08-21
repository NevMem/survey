package com.nevmem.survey.config.internal

import com.nevmem.survey.cloud.MessagingService
import com.nevmem.survey.config.Config
import com.nevmem.survey.config.ConfigProvider
import com.nevmem.survey.s3client.S3ClientWrapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory

private const val UPDATE_DELAY = 1000L

internal class ConfigProviderImpl(
    scope: CoroutineScope,
    private val s3ClientWrapper: S3ClientWrapper,
    private val messagingService: MessagingService,
) : ConfigProvider {

    private val logger by lazy {
        LoggerFactory.getLogger("ConfigProviderImpl")
    }

    init {
        logger.warn("ConfigProviderImpl constructor")
    }

    private val bucket by lazy {
        runBlocking {
            logger.warn("Getting config bucket")
            val result = s3ClientWrapper.getOrCreateBucket("ethnosurvey-config")
            logger.warn("Bucket ready")
            result
        }
    }

    private val parser by lazy {
        Json {
            ignoreUnknownKeys = false
        }
    }

    override val config: Flow<Config> = createConfigFlow().shareIn(scope, SharingStarted.Eagerly, 1)

    private fun createConfigFlow(): Flow<Config> = flow {
        var currentConfig: Config? = null
        var previousRequestFailed = false

        while (true) {
            try {
                val json = bucket.getFileContents("config.json")
                val config = parser.decodeFromString<Config>(Config.serializer(), json)

                if (currentConfig == null || currentConfig.version < config.version) {
                    currentConfig = config
                    messagingService.sendMessage("Config updated to version ${currentConfig.version}")
                    emit(config)
                }
                previousRequestFailed = false
            } catch (exception: Exception) {
                logger.warn(exception.toString())
                if (!previousRequestFailed) {
                    messagingService.sendMessage("Config downloading failed with exception: ${exception.message}")
                }
                previousRequestFailed = true
            }
            delay(UPDATE_DELAY)
        }
    }
}
