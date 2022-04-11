package com.nevmem.survey.routing

import com.nevmem.survey.role.RoleEntity
import com.nevmem.survey.role.RoleModel
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.auth.jwt.JWTPrincipal
import io.ktor.auth.principal
import io.ktor.util.pipeline.PipelineContext

fun PipelineContext<*, ApplicationCall>.userId() = call.principal<JWTPrincipal>()!!["user_id"]!!.toLong()

// suspend fun PipelineContext<*, ApplicationCall>.checkRoles(
//    roleModel: RoleModel,
//    usersService: UsersService,
//    roles: List<String>,
// ) {
//    val user = usersService.getUserById(userId())!!
//
//    if (!roleModel.hasAccess(roles.map { roleModel.roleById(it) }, user.roles)) {
//        throw IllegalStateException("Access to method denied (not enough roles)")
//    }
// }

fun List<String>.toRoles(roleModel: RoleModel): List<RoleEntity> = map { roleModel.roleById(it) }
