package com.nevmem.survey.role.internal

import com.nevmem.survey.role.RoleEntity
import com.nevmem.survey.role.RoleModel
import com.nevmem.survey.role.RoleSerializer

internal class RoleSerializerImpl(private val roleModel: RoleModel) : RoleSerializer {

    override fun rolesToString(roles: List<RoleEntity>): String =
        roles.joinToString(",") { it.roleId }

    override fun stringToRoles(rolesString: String): List<RoleEntity> =
        rolesString.split(",").filter { it.isNotEmpty() }.map { roleModel.roleById(it) }
}
