package com.nevmem.survey.service.invites.internal

import com.nevmem.survey.service.invites.InvitesService
import com.nevmem.survey.service.invites.data.InviteEntity
import com.nevmem.survey.service.users.UsersService
import com.nevmem.survey.service.users.data.UserEntity
import java.util.UUID
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal class InvitesServiceImpl : InvitesService, KoinComponent {
    private val usersService by inject<UsersService>()

    override suspend fun createInvite(owner: UserEntity, expirationSeconds: Long): InviteEntity {
        val dto = transaction {
            InviteEntityDTO.new {
                inviteId = UUID.randomUUID().toString().drop(8)
                createdAt = System.currentTimeMillis()
                expirationPeriod = expirationSeconds
                ownerId = owner.id
                acceptedByUserId = null
            }
        }
        return dto.toEntity()
    }

    override suspend fun getInviteById(inviteId: String): InviteEntity? {
        return transaction {
            InviteEntityDTO.find {
                InvitesTable.inviteId like inviteId
            }
        }.firstOrNull()?.toEntity()
    }

    override suspend fun acceptedBy(inviteEntity: InviteEntity, user: UserEntity) {
        transaction {
            InvitesTable.update({ InvitesTable.inviteId like inviteEntity.inviteId }) {
                it[acceptedByUserId] = user.id
            }
        }
    }

    private fun InviteEntityDTO.toEntity(): InviteEntity {
        return InviteEntity(
            inviteId,
            createdAt,
            expirationPeriod,
            owner = runBlocking { usersService.getUserById(ownerId)!! },
            acceptedBy = runBlocking { acceptedByUserId?.let { usersService.getUserById(it) } },
            expired = System.currentTimeMillis() - expirationPeriod > createdAt,
        )
    }
}