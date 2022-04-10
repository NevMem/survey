package com.nevmem.survey.survey

import com.nevmem.survey.project.ProjectEntity
import com.nevmem.survey.role.RoleEntity
import com.nevmem.survey.user.UserEntity

interface ProjectsService {
    suspend fun get(projectId: Long): ProjectEntity?

    suspend fun createProject(name: String, owner: UserEntity): ProjectEntity
    suspend fun projects(user: UserEntity): List<ProjectEntity>

    suspend fun getRolesInProject(project: ProjectEntity, user: UserEntity): List<RoleEntity>

    suspend fun isUserInvitedToProject(project: ProjectEntity, user: UserEntity): Boolean
}