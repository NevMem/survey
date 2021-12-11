package com.nevmem.survey.role

interface RoleModel {
    fun isAncestorRole(maybeAncestor: RoleEntity, role: RoleEntity): Boolean
    fun roleById(roleId: String): RoleEntity
    val roles: List<RoleEntity>
}
