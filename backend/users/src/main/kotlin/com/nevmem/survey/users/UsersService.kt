package com.nevmem.survey.users

import com.nevmem.survey.role.RoleEntity
import com.nevmem.survey.user.UserEntity

interface UsersService {
    data class Credentials(
        val login: String,
        val password: String,
    )

    data class Personal(
        val name: String,
        val surname: String,
        val email: String,
    )

    suspend fun createUser(
        credentials: Credentials,
        personal: Personal,
        roles: List<RoleEntity>,
    ): UserEntity

    suspend fun hasUserWithCredentials(credentials: Credentials): Boolean

    suspend fun getUserWithCredentials(credentials: Credentials): UserEntity?

    suspend fun getUserById(id: Long): UserEntity?

    suspend fun updateUserRoles(user: UserEntity, newRoles: List<RoleEntity>)
}
