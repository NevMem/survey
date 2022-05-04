package com.nevmem.survey.survey.internal

import com.nevmem.survey.TableNames
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable

internal object ProjectsTable : LongIdTable(TableNames.projectsTableName) {
    val name = varchar("name", 128)
    val ownerId = long("ownerId")
}

internal class ProjectEntityDTO(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<ProjectEntityDTO>(ProjectsTable)

    var name by ProjectsTable.name
    var ownerId by ProjectsTable.ownerId
}

internal object UserProjectAssignTable : LongIdTable(TableNames.userProjectAssignTableName) {
    val userId = long("userId")
    val projectId = long("projectId")
}

internal class UserProjectAssignDTO(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<UserProjectAssignDTO>(UserProjectAssignTable)

    var userId by UserProjectAssignTable.userId
    var projectId by UserProjectAssignTable.projectId
}

internal object UserProjectRoleTable : LongIdTable(TableNames.userProjectRoleTableName) {
    val userId = long("userId")
    val projectId = long("projectId")
    val roleId = varchar("roleId", 16)
}

internal class UserProjectRoleDTO(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<UserProjectRoleDTO>(UserProjectRoleTable)

    var userId by UserProjectRoleTable.userId
    var projectId by UserProjectRoleTable.projectId
    var roleId by UserProjectRoleTable.roleId
}
