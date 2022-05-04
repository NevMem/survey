package com.nevmem.survey.survey

import com.nevmem.survey.role.RoleModel
import com.nevmem.survey.survey.internal.ProjectsServiceImpl
import com.nevmem.survey.survey.internal.ProjectsTable
import com.nevmem.survey.survey.internal.UserProjectAssignTable
import com.nevmem.survey.survey.internal.UserProjectRoleTable
import com.nevmem.survey.users.UsersService
import org.jetbrains.exposed.sql.Table

fun createProjectsService(
    usersService: UsersService,
    roleModel: RoleModel,
): ProjectsService = ProjectsServiceImpl(usersService, roleModel)

fun projectsTables(): List<Table> = listOf(
    ProjectsTable,
    UserProjectRoleTable,
    UserProjectAssignTable,
)
