package com.nevmem.survey.service.media

import com.nevmem.survey.media.MediaEntity
import com.nevmem.survey.media.MediaGalleryEntity
import java.io.File

interface MediaStorageService {
    suspend fun uploadFileToMediaStorage(file: File): MediaEntity
    suspend fun createMediaGallery(medias: List<MediaEntity>): MediaGalleryEntity
}
