package com.nevmem.survey.service.media.internal

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable

internal object MediaTable : LongIdTable() {
    val bucketName = varchar("bucketName", 32)
    val filename = varchar("filename", 128)
}

internal class MediaEntityDTO(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<MediaEntityDTO>(MediaTable)

    var bucketName by MediaTable.bucketName
    var filename by MediaTable.filename
}
