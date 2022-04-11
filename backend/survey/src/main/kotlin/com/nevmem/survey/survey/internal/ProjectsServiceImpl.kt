package com.nevmem.survey.survey.internal

import com.nevmem.survey.project.ProjectEntity
import com.nevmem.survey.role.RoleEntity
import com.nevmem.survey.role.RoleModel
import com.nevmem.survey.survey.ProjectsService
import com.nevmem.survey.user.UserEntity
import com.nevmem.survey.users.UsersService
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction

internal class ProjectsServiceImpl(
    private val usersService: UsersService,
    private val roleModel: RoleModel,
) : ProjectsService {
    override suspend fun get(projectId: Long): ProjectEntity? = transaction {
        ProjectEntityDTO.find {
            ProjectsTable.id eq projectId
        }.firstOrNull()
    }?.toEntity()

    override suspend fun createProject(name: String, owner: UserEntity): ProjectEntity = transaction {
        val dto = ProjectEntityDTO.new {
            this.name = name
            this.ownerId = owner.id
        }

        UserProjectAssignDTO.new {
            this.projectId = dto.id.value
            this.userId = owner.id
        }

        UserProjectRoleDTO.new {
            this.projectId = dto.id.value
            this.userId = owner.id
            this.roleId = roleModel.ownerRole.roleId
        }

        dto
    }.toEntity()

    override suspend fun projects(user: UserEntity): List<ProjectEntity> = transaction {
        UserProjectAssignDTO.find {
            UserProjectAssignTable.userId eq user.id
        }.mapNotNull {
            ProjectEntityDTO.find {
                ProjectsTable.id eq it.projectId
            }.firstOrNull()
        }
    }.map { it.toEntity() }

    override suspend fun getRolesInProject(
        project: ProjectEntity,
        user: UserEntity
    ): List<RoleEntity> = transaction {
        UserProjectRoleDTO.find {
            (UserProjectRoleTable.projectId eq project.id) and (UserProjectRoleTable.userId eq user.id)
        }.map { it.roleId }.map { roleModel.roleById(it) }
    }

    override suspend fun isUserInvitedToProject(project: ProjectEntity, user: UserEntity): Boolean = transaction {
        UserProjectAssignDTO.find {
            (UserProjectAssignTable.userId eq user.id) and (UserProjectAssignTable.projectId eq project.id)
        }.firstOrNull() != null
    }

    override suspend fun addUserToProject(project: ProjectEntity, user: UserEntity) {
        transaction {
            UserProjectAssignDTO.new {
                this.projectId = project.id
                this.userId = user.id
            }
        }
    }

    private suspend fun ProjectEntityDTO.toEntity(): ProjectEntity {
        return ProjectEntity(
            id = this.id.value,
            name = this.name,
            owner = usersService.getUserById(this.ownerId)!!,
        )
    }
}
