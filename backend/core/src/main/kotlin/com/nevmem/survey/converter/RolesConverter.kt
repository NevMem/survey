package com.nevmem.survey.converter

import com.nevmem.survey.data.role.Role
import com.nevmem.survey.role.RoleEntity
import org.koin.core.component.KoinComponent

class RolesConverter : KoinComponent {
    fun convertRole(roleEntity: RoleEntity): Role {
        return Role(
            roleEntity.roleId,
        )
    }
}
