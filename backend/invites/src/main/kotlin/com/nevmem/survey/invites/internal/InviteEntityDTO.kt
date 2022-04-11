package com.nevmem.survey.invites.internal

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable

internal object InvitesTable : LongIdTable() {
    val projectId = long("projectId")
    val fromUserId = long("fromUserId")
    val toUserId = long("toUserId")
    val createdAt = long("createdAt")
    val expirationPeriod = long("expirationPeriod")
    val accepted = bool("accepted")
}

internal class InviteEntityDTO(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<InviteEntityDTO>(InvitesTable)

    var projectId by InvitesTable.projectId
    var fromUserId by InvitesTable.fromUserId
    var toUserId by InvitesTable.toUserId
    var createdAt by InvitesTable.createdAt
    var expirationPeriod by InvitesTable.expirationPeriod
    var accepted by InvitesTable.accepted
}
