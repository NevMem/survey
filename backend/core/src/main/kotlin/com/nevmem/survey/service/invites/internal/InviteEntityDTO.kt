package com.nevmem.survey.service.invites.internal

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable

internal object InvitesTable : LongIdTable() {
    val inviteId = varchar("inviteId",16)
    val createdAt = long("createdAt")
    val expirationPeriod = long("expirationPeriod")
    val ownerId = long("ownerId")
    val acceptedByUserId = long("acceptedByUserId")
}

internal class InviteEntityDTO(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<InviteEntityDTO>(InvitesTable)

    val inviteId by InvitesTable.inviteId
    val createdAt by InvitesTable.createdAt
    val expirationPeriod by InvitesTable.expirationPeriod
    val ownerId by InvitesTable.ownerId
    val acceptedByUserId by InvitesTable.acceptedByUserId
}
