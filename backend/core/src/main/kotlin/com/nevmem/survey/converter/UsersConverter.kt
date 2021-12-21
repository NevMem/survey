package com.nevmem.survey.converter

import com.nevmem.survey.data.user.User
import com.nevmem.survey.user.UserEntity
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class UsersConverter : KoinComponent {

    private val rolesConverter by inject<RolesConverter>()

    fun convertUser(userEntity: UserEntity): User {
        return User(
            id = userEntity.id,
            login = userEntity.login,
            name = userEntity.name,
            surname = userEntity.surname,
            email = userEntity.email,
            roles = userEntity.roles.map { rolesConverter.convertRole(it) },
        )
    }
}
