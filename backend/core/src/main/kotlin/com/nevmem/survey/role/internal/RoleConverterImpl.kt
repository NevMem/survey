package com.nevmem.survey.role.internal

import com.nevmem.survey.role.RoleConverter
import com.nevmem.survey.role.RoleEntity
import com.nevmem.survey.role.RoleModel
import org.koin.core.component.KoinComponent

internal class RoleConverterImpl(private val roleModel: RoleModel) : RoleConverter, KoinComponent {

    override fun rolesToString(roles: List<RoleEntity>): String =
        roles.joinToString(",") { it.roleId }

    override fun stringToRoles(rolesString: String): List<RoleEntity> =
        rolesString.split(",").map { roleModel.roleById(it) }
}