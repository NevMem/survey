package com.nevmem.surveys.converters

import com.nevmem.survey.data.role.Role
import com.nevmem.survey.role.RoleEntity

class RolesConverter {
    fun convertRole(roleEntity: RoleEntity): Role {
        return Role(
            roleEntity.roleId,
        )
    }

    operator fun invoke(roleEntity: RoleEntity) = convertRole(roleEntity)
}
