package com.nevmem.survey.s3client.sample

import com.nevmem.survey.s3client.S3ClientWrapper
import kotlinx.coroutines.runBlocking

fun main() {
    val deps = object : S3ClientWrapper.Dependencies {
        override val region: String = "ru-central1"
        override val uri: String = "https://storage.yandexcloud.net"
        override val keyId: String = "YCAJEEcuLL55V96S0VuNNoe8F"
        override val secretAccessKey: String = "YCNRGAd6tJl-j4ckSn9h4NOD-8Flh6HJlxLRXx15"
    }
    val client = S3ClientWrapper.Factory.create(deps)
    runBlocking {
        val hasBucketResponse = client.hasBucket("ethnosurvey-config")
        println(hasBucketResponse)

        val bucket = client.getOrCreateBucket("ethnosurvey-config")
        println(bucket)

        val content = "Some content"
        bucket.putFileContents("some-file.txt", content)
        println(bucket.getFileContents("some-file.txt"))
    }
}
