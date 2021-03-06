package com.nevmem.survey.users.internal

import com.nevmem.survey.auth.PasswordEncoder
import com.nevmem.survey.role.RoleEntity
import com.nevmem.survey.user.UserEntity
import com.nevmem.survey.users.UsersService
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal class UsersServiceImpl : UsersService, KoinComponent {
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
            }
        }
        return UserEntity(
            dto.id.value,
            dto.login,
            dto.name,
            dto.surname,
            dto.email,
        )
    }

    override suspend fun getUserWithCredentials(credentials: UsersService.Credentials): UserEntity? {
        return transaction {
            UserEntityDTO.find {
                (UsersTable.login like credentials.login) and
                    (UsersTable.password like passwordEncoder.encodePassword(credentials.password))
            }
                .firstOrNull()
                ?.toEntity()
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

    override suspend fun getUserByLogin(login: String): UserEntity? {
        return transaction {
            UserEntityDTO.find {
                UsersTable.login eq login
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
        )
    }
}
