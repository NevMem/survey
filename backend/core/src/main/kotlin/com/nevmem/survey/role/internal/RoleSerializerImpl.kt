package com.nevmem.survey.role.internal

import com.nevmem.survey.role.RoleSerializer
import com.nevmem.survey.role.RoleEntity
import com.nevmem.survey.role.RoleModel
import org.koin.core.component.KoinComponent

internal class RoleSerializerImpl(private val roleModel: RoleModel) : RoleSerializer, KoinComponent {

    override fun rolesToString(roles: List<RoleEntity>): String =
        roles.joinToString(",") { it.roleId }

    override fun stringToRoles(rolesString: String): List<RoleEntity> =
        rolesString.split(",").filter { it.isNotEmpty() }.map { roleModel.roleById(it) }
}
