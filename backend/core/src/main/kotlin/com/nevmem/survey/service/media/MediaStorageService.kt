package com.nevmem.survey.service.media

import com.nevmem.survey.media.MediaEntity
import java.io.File

interface MediaStorageService {
    suspend fun uploadFileToMediaStorage(file: File): MediaEntity
}
