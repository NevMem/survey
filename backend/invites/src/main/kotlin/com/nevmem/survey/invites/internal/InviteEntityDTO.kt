package com.nevmem.survey.invites.internal

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable

object InvitesTable : LongIdTable() {
    val inviteId = varchar("inviteId", 16)
    val createdAt = long("createdAt")
    val expirationPeriod = long("expirationPeriod")
    val ownerId = long("ownerId")
    val acceptedByUserId = long("acceptedByUserId").nullable()
}

internal class InviteEntityDTO(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<InviteEntityDTO>(InvitesTable)

    var inviteId by InvitesTable.inviteId
    var createdAt by InvitesTable.createdAt
    var expirationPeriod by InvitesTable.expirationPeriod
    var ownerId by InvitesTable.ownerId
    var acceptedByUserId by InvitesTable.acceptedByUserId
}
