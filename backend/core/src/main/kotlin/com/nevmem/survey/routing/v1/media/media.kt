package com.nevmem.survey.routing.v1.media

import com.nevmem.survey.data.media.Media
import com.nevmem.survey.service.fs.FileSystemService
import com.nevmem.survey.service.media.MediaStorageService
import io.ktor.application.call
import io.ktor.request.receiveMultipart
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.post
import org.koin.ktor.ext.inject

fun Route.media() {
    val fsService by inject<FileSystemService>()
    val mediaService by inject<MediaStorageService>()

    post("/upload") {
        val multipart = call.receiveMultipart()
        val file = fsService.saveFromMultiPart(multipart)
        val media = mediaService.uploadFileToMediaStorage(file)
        call.respond(Media(media.filename, media.url))
    }
}
