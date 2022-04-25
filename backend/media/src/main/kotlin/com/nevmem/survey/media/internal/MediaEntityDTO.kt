package com.nevmem.survey.media.internal

import com.nevmem.survey.TableNames
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable

internal object MediaTable : LongIdTable(TableNames.mediaTableName) {
    val bucketName = varchar("bucketName", 32)
    val filename = varchar("filename", 128)
}

internal object MediaGalleryTable : LongIdTable(TableNames.mediaGalleryTableName) {
    val medias = varchar("medias", 512)
}

internal class MediaEntityDTO(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<MediaEntityDTO>(MediaTable)

    var bucketName by MediaTable.bucketName
    var filename by MediaTable.filename
}

internal class MediaGalleryDTO(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<MediaGalleryDTO>(MediaGalleryTable)

    var medias by MediaGalleryTable.medias
}
