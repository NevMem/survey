package com.nevmem.survey.s3client.internal

import com.nevmem.survey.s3client.Bucket
import com.nevmem.survey.s3client.HasBucketResponse
import com.nevmem.survey.s3client.S3ClientWrapper
import kotlinx.coroutines.delay
import software.amazon.awssdk.auth.credentials.AwsCredentials
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.S3Configuration
import software.amazon.awssdk.services.s3.model.CreateBucketConfiguration
import software.amazon.awssdk.services.s3.model.CreateBucketRequest
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.model.HeadBucketRequest
import software.amazon.awssdk.services.s3.model.NoSuchBucketException
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import java.net.URI
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

private class BucketImpl(
    private val client: S3Client,
    private val bucketName: String,
): Bucket {
    override suspend fun getFileContents(filename: String): String = suspendCoroutine { continuation ->
        val response = client.getObject(GetObjectRequest.builder().bucket(bucketName).key(filename).build())

        val sb = StringBuilder()
        val array = ByteArray(1024)
        var readLen = response.read(array)
        while (readLen > 0) {
            sb.append(String(array, 0, readLen))
            readLen = response.read(array)
        }

        continuation.resume(sb.toString())
    }

    override suspend fun putFileContents(filename: String, content: String): Unit = suspendCoroutine { continuation ->
        client.putObject(
            PutObjectRequest.builder().bucket(bucketName).key(filename).build(),
            RequestBody.fromString(content),
        )
        continuation.resume(Unit)
    }
}

internal class S3ClientWrapperImpl(
    private val deps: S3ClientWrapper.Dependencies,
) : S3ClientWrapper {

    private val client: S3Client by lazy {
        S3Client.builder()
            .region(Region.of(deps.region))
            .serviceConfiguration(S3Configuration.builder().pathStyleAccessEnabled(true).build())
            .endpointOverride(URI.create(deps.uri))
            .credentialsProvider {
                object : AwsCredentials {
                    override fun accessKeyId(): String = deps.keyId
                    override fun secretAccessKey(): String = deps.secretAccessKey
                }
            }
            .build()
    }

    override suspend fun hasBucket(name: String): HasBucketResponse = suspendCoroutine { continuation ->
        try {
            client.headBucket(HeadBucketRequest.builder().bucket(name).build())
            continuation.resume(HasBucketResponse.Exists)
        } catch (exception: NoSuchBucketException) {
            continuation.resume(HasBucketResponse.NotFound)
        } catch (exception: Exception) {
            continuation.resume(HasBucketResponse.SdkError(exception))
        }
    }

    override suspend fun getBucket(name: String): Bucket {
        val hasBucket = hasBucketWithRetry(name)
        assert(hasBucket is HasBucketResponse.Exists)
        return BucketImpl(client, name)
    }

    override suspend fun getOrCreateBucket(name: String): Bucket {
        val hasBucket = hasBucketWithRetry(name)
        if (hasBucket is HasBucketResponse.NotFound) {
            createBucket(name)
        }
        return getBucket(name)
    }

    private suspend fun createBucket(name: String): Unit = suspendCoroutine { continuation ->
        client.createBucket(
            CreateBucketRequest.builder()
                .bucket(name)
                .createBucketConfiguration(
                    CreateBucketConfiguration.builder()
                        .locationConstraint(deps.region)
                        .build()
                )
                .build()
        )
        client.waiter().waitUntilBucketExists(
            HeadBucketRequest.builder()
                .bucket(name)
                .build()
        )
        continuation.resume(Unit)
    }

    private suspend fun hasBucketWithRetry(name: String): HasBucketResponse {
        var response = hasBucket(name)
        var delayTime = 1000L
        while (response is HasBucketResponse.SdkError) {
            delay(delayTime)
            response = hasBucket(name)
            delayTime = (delayTime * 2).coerceAtMost(60 * 1000L)
        }
        return response
    }
}