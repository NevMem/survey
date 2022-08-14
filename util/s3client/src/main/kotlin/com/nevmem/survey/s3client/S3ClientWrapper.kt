package com.nevmem.survey.s3client

import com.nevmem.survey.s3client.internal.S3ClientWrapperImpl

interface Bucket {
    suspend fun getFileContents(filename: String): String
    suspend fun putFileContents(filename: String, content: String)
}

sealed class HasBucketResponse {
    object Exists: HasBucketResponse()
    object NotFound: HasBucketResponse()
    data class SdkError(val exception: Exception): HasBucketResponse()
}

interface S3ClientWrapper {
    interface Dependencies {
        val region: String
        val uri: String
        val keyId: String
        val secretAccessKey: String
    }

    companion object Factory {
        fun create(deps: Dependencies): S3ClientWrapper = S3ClientWrapperImpl(deps)
    }

    suspend fun hasBucket(name: String): HasBucketResponse
    suspend fun getBucket(name: String): Bucket
    suspend fun getOrCreateBucket(name: String): Bucket
}