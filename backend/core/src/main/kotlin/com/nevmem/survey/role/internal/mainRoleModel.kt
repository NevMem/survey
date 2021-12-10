package com.nevmem.survey.role.internal

import com.nevmem.survey.role.RoleEntity
import com.nevmem.survey.role.RoleModel

typealias RoleEntityVisitor = (RoleEntity) -> Unit

private class RoleEntityBuilder(
    private val currentRole: RoleEntity,
    private val visitor: RoleEntityVisitor,
) {
    init {
        visitor(currentRole)
    }

    fun role(roleId: String, builder: RoleEntityBuilder.() -> Unit = {}) {
        val childRole = RoleEntity(roleId, currentRole)
        builder(RoleEntityBuilder(childRole, visitor))
    }
}

private fun role(
    roleId: String,
    visitor: RoleEntityVisitor,
    builder: RoleEntityBuilder.() -> Unit,
): RoleEntity {
    val rootRole = RoleEntity(roleId, null)
    builder(RoleEntityBuilder(rootRole, visitor))
    return rootRole
}

internal fun mainRoleModel(): RoleModel {
    val allRoles = mutableListOf<RoleEntity>()
    role("admin", { allRoles += it }) {
        role("push.manager") {
            role("push.schedule")
            role("push.observe")
        }

        role("survey.manager") {
            role("survey.create")
            role("survey.activate")
        }

        role("role.manager")

        role("invite.manager")
    }

    return object : RoleModel {
        override fun isAncestorRole(maybeAncestor: RoleEntity, role: RoleEntity): Boolean {
            var current: RoleEntity? = role
            while (current != null) {
                if (current == maybeAncestor) {
                    return true
                }
                current = current.parentRole
            }
            return false
        }

        override fun roleById(roleId: String): RoleEntity {
            return allRoles.find { it.roleId == roleId } ?:
                throw IllegalStateException("No role for given id: $roleId")
        }
    }
}
