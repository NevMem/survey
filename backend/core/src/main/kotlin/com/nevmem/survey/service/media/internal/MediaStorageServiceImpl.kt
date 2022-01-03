package com.nevmem.survey.service.media.internal

import com.nevmem.survey.env.EnvVars
import com.nevmem.survey.media.MediaEntity
import com.nevmem.survey.service.media.MediaStorageService
import java.io.File
import java.net.URI
import software.amazon.awssdk.auth.credentials.AwsCredentials
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.S3Configuration
import software.amazon.awssdk.services.s3.model.CreateBucketConfiguration
import software.amazon.awssdk.services.s3.model.CreateBucketRequest
import software.amazon.awssdk.services.s3.model.HeadBucketRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest

internal class MediaStorageServiceImpl : MediaStorageService {
    private val region = Region.of(EnvVars.S3.region)
    private val client: S3Client = S3Client.builder()
        .region(region)
        .serviceConfiguration(S3Configuration.builder().pathStyleAccessEnabled(true).build())
        .endpointOverride(URI.create(EnvVars.S3.s3Uri))
        .credentialsProvider {
            object : AwsCredentials {
                override fun accessKeyId(): String = EnvVars.S3.accessKey
                override fun secretAccessKey(): String = EnvVars.S3.secretAccessKey
            }
        }
        .build()

    private val bucketName = "media"

    init {
        println("Initializing bucket")
        try {
            client.createBucket(
                CreateBucketRequest.builder()
                    .bucket(bucketName)
                    .createBucketConfiguration(
                        CreateBucketConfiguration.builder()
                            .locationConstraint(region.id())
                            .build()
                    )
                    .build()
            )
            client.waiter().waitUntilBucketExists(
                HeadBucketRequest.builder()
                    .bucket(bucketName)
                    .build()
            )
            println("Bucket initialized")
        } catch (exception: Exception) {
            println(exception)
        }
    }

    override suspend fun uploadFileToMediaStorage(file: File): MediaEntity {
        try {
            client.putObject(
                PutObjectRequest.builder().bucket(bucketName).key(file.name).build(),
                RequestBody.fromFile(file)
            )
            return MediaEntity(file.name)
        } catch (exception: Exception) {
            println("Exception occurred while uploading")
            println(exception)
            throw IllegalStateException("Upload to media storage failed")
        }
    }
}
