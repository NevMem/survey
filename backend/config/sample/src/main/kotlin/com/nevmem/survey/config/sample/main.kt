package com.nevmem.survey.config.sample

import com.nevmem.survey.cloud.CloudServices
import com.nevmem.survey.config.ConfigProvider
import com.nevmem.survey.s3client.S3ClientWrapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.runBlocking

fun main() {
    val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    val deps = object : S3ClientWrapper.Dependencies {
        override val region: String = "ru-central1"
        override val uri: String = "https://storage.yandexcloud.net"
        override val keyId: String = "YCAJEEcuLL55V96S0VuNNoe8F"
        override val secretAccessKey: String = "YCNRGAd6tJl-j4ckSn9h4NOD-8Flh6HJlxLRXx15"
    }
    val s3ClientWrapper = S3ClientWrapper.Factory.create(deps)
    val configProvider = ConfigProvider.Factory.create(scope, s3ClientWrapper, CloudServices.Factory.create().messaging)

    runBlocking {
        configProvider.config.collect {
            println(it)
        }
    }
}
