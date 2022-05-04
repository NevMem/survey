package com.nevmem.survey.media

import java.io.File

interface MediaStorageService {
    suspend fun uploadFileToMediaStorage(file: File): MediaEntity
    suspend fun createMediaGallery(medias: List<MediaEntity>): MediaGalleryEntity
    suspend fun downloadToFile(file: File, entity: MediaEntity)
    fun mediaById(id: Long): MediaEntity?

    suspend fun mediaGallery(id: Long): MediaGalleryEntity?
}
