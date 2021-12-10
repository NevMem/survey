package com.nevmem.survey.service.users.internal

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable

internal object UsersTable : LongIdTable() {
    val login = varchar("login", 32)
    val password = varchar("password", 64)
    val name = varchar("name", 32)
    val surname = varchar("surname", 32)
    val email = varchar("email", 64)
    val roles = text("roles")
}

internal class UserEntityDTO(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<UserEntityDTO>(UsersTable)

    var login by UsersTable.login
    var password by UsersTable.password
    var name by UsersTable.name
    var surname by UsersTable.surname
    var email by UsersTable.email
    var roles by UsersTable.roles
}