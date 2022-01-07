package com.nevmem.survey.routing.roles

import com.nevmem.survey.converter.RolesConverter
import com.nevmem.survey.data.response.role.AllRolesResponse
import com.nevmem.survey.role.RoleModel
import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import org.koin.ktor.ext.inject

fun Route.allRoles() {
    val roleModel by inject<RoleModel>()
    val rolesConverter by inject<RolesConverter>()
    get("/roles") {
        call.respond(AllRolesResponse(roleModel.roles.map { rolesConverter(it) }))
    }
}
