package com.nevmem.survey.config.internal

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

private const val UPDATE_DELAY = 1000L

internal class ConfigProviderImpl(
    scope: CoroutineScope,
    private val s3ClientWrapper: S3ClientWrapper,
) : ConfigProvider {

    private val bucket by lazy {
        runBlocking {
            s3ClientWrapper.getOrCreateBucket("ethnosurvey-config")
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

        while (true) {
            try {
                val json = bucket.getFileContents("config.json")
                val config = parser.decodeFromString<Config>(Config.serializer(), json)

                if (currentConfig == null || currentConfig.version < config.version) {
                    currentConfig = config
                    emit(config)
                }
            } catch (exception: Exception) {
                println(exception)
            }
            delay(UPDATE_DELAY)
        }
    }
}
