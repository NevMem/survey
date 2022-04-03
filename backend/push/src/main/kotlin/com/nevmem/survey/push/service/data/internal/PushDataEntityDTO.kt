package com.nevmem.survey.push.service.data.internal

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable

internal object PushDataTable : LongIdTable() {
    val uid = varchar("uid", 64)
    val token = varchar("token", 256).nullable()
}

internal class PushDataEntityDTO(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<PushDataEntityDTO>(PushDataTable)

    var uid by PushDataTable.uid
    var token by PushDataTable.token
}
