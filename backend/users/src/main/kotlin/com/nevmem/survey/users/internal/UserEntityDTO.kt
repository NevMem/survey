package com.nevmem.survey.users.internal

import com.nevmem.survey.TableNames
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable

internal object UsersTable : LongIdTable(TableNames.userTableName) {
    val login = varchar("login", 32)
    val password = varchar("password", 64)
    val name = varchar("name", 32)
    val surname = varchar("surname", 32)
    val email = varchar("email", 64)
}

internal class UserEntityDTO(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<UserEntityDTO>(UsersTable)

    var login by UsersTable.login
    var password by UsersTable.password
    var name by UsersTable.name
    var surname by UsersTable.surname
    var email by UsersTable.email
}
