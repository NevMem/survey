package com.nevmem.survey.role

interface RoleSerializer {
    fun rolesToString(roles: List<RoleEntity>): String
    fun stringToRoles(rolesString: String): List<RoleEntity>
}
