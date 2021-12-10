package com.nevmem.survey.role

interface RoleConverter {
    fun rolesToString(roles: List<RoleEntity>): String
    fun stringToRoles(rolesString: String): List<RoleEntity>
}