package com.nevmem.survey.media.internal

import com.nevmem.survey.env.EnvVars
import com.nevmem.survey.media.MediaEntity
import com.nevmem.survey.media.MediaGalleryEntity
import com.nevmem.survey.media.MediaStorageService
import org.jetbrains.exposed.sql.transactions.transaction
import software.amazon.awssdk.auth.credentials.AwsCredentials
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.S3Configuration
import software.amazon.awssdk.services.s3.model.CreateBucketConfiguration
import software.amazon.awssdk.services.s3.model.CreateBucketRequest
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.model.HeadBucketRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import java.io.File
import java.net.URI

private class MediaUrlCreator {
    fun createUrl(dto: MediaEntityDTO): String {
        return "${EnvVars.Self.uri}/v1/media/get/${dto.id.value}"
    }
}

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

    private val mediaUrlCreator = MediaUrlCreator()

    private val bucketName = "ethnosurvey-media"

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

            val dto = transaction {
                MediaEntityDTO.new {
                    filename = file.name
                    bucketName = this@MediaStorageServiceImpl.bucketName
                }
            }

            return dto.entity
        } catch (exception: Exception) {
            println("Exception occurred while uploading")
            println(exception)
            throw IllegalStateException("Upload to media storage failed")
        }
    }

    override suspend fun createMediaGallery(medias: List<MediaEntity>): MediaGalleryEntity = transaction {
        val dtos = medias.mapNotNull { entity ->
            MediaEntityDTO.find {
                MediaTable.id eq entity.id
            }.firstOrNull()
        }
        val galleryDto: Pair<List<MediaEntityDTO>, MediaGalleryDTO> = dtos to MediaGalleryDTO.new {
            this.medias = dtos.map { it.id.value.toString() }.joinToString(",")
        }

        MediaGalleryEntity(
            id = galleryDto.second.id.value,
            medias = galleryDto.second.medias.split(",")
                .map { it.toLong() }
                .mapNotNull { id ->
                    galleryDto.first.find { it.id.value == id }
                }
                .map { it.entity },
        )
    }

    override suspend fun downloadToFile(file: File, entity: MediaEntity) {
        val response = client.getObject(
            GetObjectRequest.builder()
                .bucket(bucketName)
                .key(entity.filename)
                .build()
        )
        file.outputStream().apply {
            var readLen = 0
            val array = ByteArray(1024)
            readLen = response.read(array)
            while (readLen > 0) {
                write(array, 0, readLen)
                readLen = response.read(array)
            }
            flush()
            close()
        }
    }

    override fun mediaById(id: Long): MediaEntity? = transaction {
        MediaEntityDTO.find {
            MediaTable.id eq id
        }.firstOrNull()?.entity
    }

    override suspend fun mediaGallery(id: Long): MediaGalleryEntity? = transaction {
        val dto = MediaGalleryDTO.find {
            MediaGalleryTable.id eq id
        }.firstOrNull() ?: return@transaction null

        MediaGalleryEntity(
            id = dto.id.value,
            medias = dto.medias.split(",")
                .map { it.toLong() }
                .mapNotNull { id ->
                    MediaEntityDTO.find {
                        MediaTable.id eq id
                    }.firstOrNull()
                }
                .map { it.entity },
        )
    }

    private val MediaEntityDTO.entity: MediaEntity
        get() = MediaEntity(
            this.id.value,
            this.filename,
            this.bucketName,
            mediaUrlCreator.createUrl(this),
        )
}
