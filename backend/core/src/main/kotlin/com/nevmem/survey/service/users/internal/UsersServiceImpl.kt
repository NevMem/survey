package com.nevmem.survey.service.users.internal

import com.nevmem.survey.role.RoleEntity
import com.nevmem.survey.role.RoleSerializer
import com.nevmem.survey.service.security.auth.PasswordEncoder
import com.nevmem.survey.service.users.UsersService
import com.nevmem.survey.service.users.data.UserEntity
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal class UsersServiceImpl : UsersService, KoinComponent {
    private val roleConverter: RoleSerializer by inject()
    private val passwordEncoder: PasswordEncoder by inject()

    override suspend fun createUser(
        credentials: UsersService.Credentials,
        personal: UsersService.Personal,
        roles: List<RoleEntity>,
    ): UserEntity {
        val dto = transaction {
            UserEntityDTO.new {
                login = credentials.login
                password = passwordEncoder.encodePassword(credentials.password)

                name = personal.name
                surname = personal.surname
                email = personal.email
                this.roles = roleConverter.rolesToString(roles)
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
            }.firstOrNull()?.toEntity()
        }
    }

    override suspend fun hasUserWithCredentials(credentials: UsersService.Credentials): Boolean {
        return getUserWithCredentials(credentials) == null
    }


    override suspend fun getUserById(id: Long): UserEntity? {
        return transaction {
            UserEntityDTO.find {
                UsersTable.id eq id
            }.firstOrNull()?.toEntity()
        }
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
