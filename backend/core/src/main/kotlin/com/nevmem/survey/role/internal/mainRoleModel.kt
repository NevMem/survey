package com.nevmem.survey.role.internal

import com.nevmem.survey.role.RoleEntity
import com.nevmem.survey.role.RoleModel
import org.slf4j.LoggerFactory

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
            role("survey.creator")
            role("survey.observer")
        }

        role("role.manager")

        role("invite.manager")
    }

    LoggerFactory.getLogger(RoleModel::class.simpleName).apply {
        allRoles.forEach {
            info("role parent: ${it.parentRole?.roleId}, child: ${it.roleId}")
        }
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

        override fun hasAccess(requiredRoles: List<RoleEntity>, userRoles: List<RoleEntity>): Boolean {
            return requiredRoles.all { required -> userRoles.any { isAncestorRole(it, required) } }
        }

        override fun roleById(roleId: String): RoleEntity {
            return allRoles.find { it.roleId == roleId }
                ?: throw IllegalStateException("No role for given id: $roleId")
        }

        override val roles: List<RoleEntity>
            get() = allRoles

        override fun findDescendantRoles(roles: List<RoleEntity>): List<RoleEntity> {
            return allRoles.filter { role -> roles.any { isAncestorRole(it, role) } }
        }
    }
}
