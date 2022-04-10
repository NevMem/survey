package com.nevmem.surveys.converters

import com.nevmem.survey.data.user.Administrator
import com.nevmem.survey.role.RoleModel
import com.nevmem.survey.user.UserEntity
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class UsersConverter : KoinComponent {

    private val rolesConverter by inject<RolesConverter>()
    private val roleModel by inject<RoleModel>()

    fun convertUser(userEntity: UserEntity): Administrator {
        return Administrator(
            id = userEntity.id,
            login = userEntity.login,
            name = userEntity.name,
            surname = userEntity.surname,
            email = userEntity.email,
            roles = userEntity.roles.map { rolesConverter.convertRole(it) },
            allAvailableRoles = roleModel.findDescendantRoles(userEntity.roles).map { rolesConverter(it) },
        )
    }

    operator fun invoke(userEntity: UserEntity) = convertUser(userEntity)
}
