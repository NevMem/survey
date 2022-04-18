package com.nevmem.survey.routing.v1.media

import com.nevmem.survey.data.request.media.CreateGalleryRequest
import com.nevmem.survey.data.response.media.CreateGalleryResponse
import com.nevmem.survey.exception.NotFoundException
import com.nevmem.survey.fs.FileSystemService
import com.nevmem.survey.media.MediaEntity
import com.nevmem.survey.media.MediaStorageService
import com.nevmem.surveys.converters.MediaConverter
import com.nevmem.surveys.converters.MediaGalleryConverter
import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.request.receiveMultipart
import io.ktor.response.respond
import io.ktor.response.respondFile
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import org.koin.ktor.ext.inject

fun Route.mediaImpl() {
    val fsService by inject<FileSystemService>()
    val mediaService by inject<MediaStorageService>()
    val mediaConverter by inject<MediaConverter>()
    val mediaGalleryConverter by inject<MediaGalleryConverter>()

    post("/upload") {
        val multipart = call.receiveMultipart()
        val file = fsService.saveFromMultiPart(multipart)
        val media = mediaService.uploadFileToMediaStorage(file)
        call.respond(mediaConverter(media))
    }

    post("/create_gallery") {
        val request = call.receive<CreateGalleryRequest>()
        val gallery = mediaService.createMediaGallery(request.gallery.map { MediaEntity(it.id, it.filename, it.bucketName, it.url) })
        call.respond(
            CreateGalleryResponse(
                gallery = mediaGalleryConverter(gallery),
            )
        )
    }

    get("/get/{mediaId}") {
        val mediaId = call.parameters["mediaId"]?.toLong() ?: throw IllegalStateException("Media id not present in path")
        val media = mediaService.mediaById(mediaId) ?: throw NotFoundException()
        val fileType: FileSystemService.FileType = FileSystemService.FileType
            .values().associateBy { it.ext }[media.filename.split(".").last()]
                ?: throw IllegalStateException("unknown file type")
        val file = fsService.createFile(fileType)
        mediaService.downloadToFile(file, media)
        call.respondFile(file)
    }
}

fun Route.media() {
    route("/media") {
        mediaImpl()
    }
}
