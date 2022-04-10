package com.nevmem.surveys.converters

import com.nevmem.survey.data.user.Administrator
import com.nevmem.survey.user.UserEntity

class UsersConverter {
    fun convertUser(userEntity: UserEntity): Administrator {
        return Administrator(
            id = userEntity.id,
            login = userEntity.login,
            name = userEntity.name,
            surname = userEntity.surname,
            email = userEntity.email,
        )
    }

    operator fun invoke(userEntity: UserEntity) = convertUser(userEntity)
}
