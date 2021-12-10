package com.nevmem.survey.service.users.internal

import com.nevmem.survey.role.RoleConverter
import com.nevmem.survey.service.users.UsersService
import com.nevmem.survey.service.users.data.UserEntity
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal class UsersServiceImpl : UsersService, KoinComponent {

    private val roleConverter: RoleConverter by inject()

    override suspend fun createUser(
        credentials: UsersService.Credentials,
        personal: UsersService.Personal
    ): UserEntity {
        val dto = transaction {
            UserEntityDTO.new {
                login = credentials.login
                password = credentials.password

                name = personal.name
                surname = personal.surname
                email = personal.email
            }
        }
        return UserEntity(
            dto.id.value,
            dto.login,
            dto.name,
            dto.surname,
            dto.email,
            roleConverter.stringToRoles(dto.roles),
        )
    }
}