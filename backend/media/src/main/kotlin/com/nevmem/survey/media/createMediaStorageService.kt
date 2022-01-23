package com.nevmem.survey.media

import com.nevmem.survey.media.internal.MediaGalleryTable
import com.nevmem.survey.media.internal.MediaStorageServiceImpl
import com.nevmem.survey.media.internal.MediaTable
import org.jetbrains.exposed.sql.Table

fun createMediaStorageService(): MediaStorageService = MediaStorageServiceImpl()

fun mediaTables(): List<Table> = listOf(
    MediaTable,
    MediaGalleryTable,
)
