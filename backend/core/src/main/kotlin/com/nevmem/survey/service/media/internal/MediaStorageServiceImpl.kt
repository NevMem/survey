package com.nevmem.survey.service.media.internal

import com.nevmem.survey.media.MediaEntity
import com.nevmem.survey.service.media.MediaStorageService
import java.io.File

internal class MediaStorageServiceImpl : MediaStorageService {
    override suspend fun uploadFileToMediaStorage(file: File): MediaEntity {
        return MediaEntity(file.name)
    }
}
