package com.nevmem.survey.service.users.internal

import com.nevmem.survey.role.RoleConverter
import com.nevmem.survey.service.security.auth.PasswordEncoder
import com.nevmem.survey.service.users.UsersService
import com.nevmem.survey.service.users.data.UserEntity
import com.nevmem.survey.service.users.internal.UsersTable.login
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal class UsersServiceImpl : UsersService, KoinComponent {
    private val roleConverter: RoleConverter by inject()
    private val passwordEncoder: PasswordEncoder by inject()

    override suspend fun createUser(
        credentials: UsersService.Credentials,
        personal: UsersService.Personal
    ): UserEntity {
        val dto = transaction {
            UserEntityDTO.new {
                login = credentials.login
                password = passwordEncoder.encodePassword(credentials.password)

                name = personal.name
                surname = personal.surname
                email = personal.email
                roles = "admin"
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

    override suspend fun getUserWithCredentials(credentials: UsersService.Credentials): UserEntity? {
        return transaction {
            UserEntityDTO.find {
                UsersTable.login like credentials.login
                UsersTable.password like passwordEncoder.encodePassword(credentials.password)
            }
        }.firstOrNull()?.toEntity()
    }

    override suspend fun hasUserWithCredentials(credentials: UsersService.Credentials): Boolean {
        return getUserWithCredentials(credentials) == null
    }


    override suspend fun getUserById(id: Long): UserEntity? {
        return transaction {
            UserEntityDTO.find {
                UsersTable.id eq id
            }
        }.firstOrNull()?.toEntity()
    }

    private fun UserEntityDTO.toEntity(): UserEntity {
        return UserEntity(
            id.value,
            login,
            name,
            surname,
            email,
            roleConverter.stringToRoles(roles),
        )
    }
}
