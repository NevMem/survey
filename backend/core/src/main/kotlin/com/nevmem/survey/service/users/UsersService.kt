package com.nevmem.survey.service.users

import com.nevmem.survey.service.users.data.UserEntity

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
    ): UserEntity

    suspend fun hasUserWithCredentials(credentials: Credentials): Boolean

    suspend fun getUserWithCredentials(credentials: Credentials): UserEntity?

    suspend fun getUserById(id: Long): UserEntity?
}
