package com.nevmem.survey.role

typealias RoleEntityVisitor = (RoleEntity) -> Unit

const val SURVEY_MANAGER = "survey.manager"

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

fun mainRoleModel(): RoleModel {
    val allRoles = mutableListOf<RoleEntity>()
    role("admin", { allRoles += it }) {
        role("push.manager") {
            role("push.schedule")
            role("push.observe")
        }

        role(SURVEY_MANAGER) {
            role("survey.creator")
            role("survey.observer")
        }

        role("role.manager")

        role("invite.manager")
    }

    return object : RoleModel {
        override val ownerRole: RoleEntity = allRoles.find { it.parentRole == null }!!

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
