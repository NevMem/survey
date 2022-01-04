package com.nevmem.survey.role

interface RoleModel {
    fun isAncestorRole(maybeAncestor: RoleEntity, role: RoleEntity): Boolean
    fun hasAccess(requiredRoles: List<RoleEntity>, userRoles: List<RoleEntity>): Boolean
    fun roleById(roleId: String): RoleEntity
    val roles: List<RoleEntity>

    fun findDescendantRoles(roles: List<RoleEntity>): List<RoleEntity>
}
