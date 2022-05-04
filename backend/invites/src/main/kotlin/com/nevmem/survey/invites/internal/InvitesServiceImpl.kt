package com.nevmem.survey.invites.internal

import com.nevmem.survey.invite.InviteEntity
import com.nevmem.survey.invite.InviteEntityStatus
import com.nevmem.survey.invites.InvitesService
import com.nevmem.survey.project.ProjectEntity
import com.nevmem.survey.survey.ProjectsService
import com.nevmem.survey.user.UserEntity
import com.nevmem.survey.users.UsersService
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal class InvitesServiceImpl(
    private val projectsService: ProjectsService,
) : InvitesService, KoinComponent {
    private val usersService by inject<UsersService>()

    override suspend fun createInvite(fromUser: UserEntity, toUser: UserEntity, project: ProjectEntity, expirationSeconds: Long): InviteEntity {
        val dto = transaction {
            InviteEntityDTO.new {
                createdAt = System.currentTimeMillis()
                projectId = project.id
                expirationPeriod = expirationSeconds * 1000
                accepted = false
                fromUserId = fromUser.id
                toUserId = toUser.id
            }
        }
        return dto.toEntity()
    }

    override suspend fun get(id: Long): InviteEntity? {
        val dto = transaction {
            InviteEntityDTO.find {
                InvitesTable.id eq id
            }.firstOrNull()
        }
        return dto?.toEntity()
    }

    override suspend fun accept(inviteEntity: InviteEntity) {
        transaction {
            InviteEntityDTO.find {
                InvitesTable.id eq inviteEntity.id
            }.firstOrNull()?.accepted = true
        }
    }

    override suspend fun incomingInvites(user: UserEntity): List<InviteEntity> = transaction {
        InviteEntityDTO.find {
            InvitesTable.toUserId eq user.id
        }.toList()
    }.map { it.toEntity() }

    override suspend fun outgoingInvites(user: UserEntity): List<InviteEntity> = transaction {
        InviteEntityDTO.find {
            InvitesTable.fromUserId eq user.id
        }.toList()
    }.map { it.toEntity() }

    private val InviteEntityDTO.status: InviteEntityStatus
        get() {
            if (accepted) {
                return InviteEntityStatus.Accepted
            }
            if (System.currentTimeMillis() > createdAt + expirationPeriod) {
                return InviteEntityStatus.Expired
            }
            return InviteEntityStatus.Waiting
        }

    private suspend fun InviteEntityDTO.toEntity(): InviteEntity {
        return InviteEntity(
            id = this.id.value,
            project = projectsService.get(this.projectId)!!,
            createdAt = this.createdAt,
            expirationPeriod = this.expirationPeriod,
            fromUser = usersService.getUserById(this.fromUserId)!!,
            toUser = usersService.getUserById(this.toUserId)!!,
            status = this.status,
        )
    }
}
