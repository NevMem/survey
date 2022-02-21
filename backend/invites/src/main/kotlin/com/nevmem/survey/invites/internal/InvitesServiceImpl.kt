package com.nevmem.survey.invites.internal

import com.nevmem.survey.invite.InviteEntity
import com.nevmem.survey.invites.InvitesService
import com.nevmem.survey.user.UserEntity
import com.nevmem.survey.users.UsersService
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.UUID

internal class InvitesServiceImpl : InvitesService, KoinComponent {
    private val usersService by inject<UsersService>()

    override suspend fun createInvite(owner: UserEntity, expirationSeconds: Long): InviteEntity {
        val dto = transaction {
            InviteEntityDTO.new {
                inviteId = UUID.randomUUID().toString().takeLast(12)
                createdAt = System.currentTimeMillis()
                expirationPeriod = expirationSeconds * 1000
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
            }.firstOrNull()?.toEntity()
        }
    }

    override suspend fun acceptedBy(inviteEntity: InviteEntity, user: UserEntity) {
        transaction {
            InvitesTable.update({ InvitesTable.inviteId like inviteEntity.inviteId }) {
                it[acceptedByUserId] = user.id
            }
        }
    }

    override suspend fun userInvites(ownerId: Long): List<InviteEntity> {
        return transaction {
            InviteEntityDTO.find {
                InvitesTable.ownerId eq ownerId
            }.map { it.toEntity() }
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
