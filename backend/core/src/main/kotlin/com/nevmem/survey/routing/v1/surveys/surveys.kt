package com.nevmem.survey.routing.v1.surveys

import com.nevmem.survey.data.request.survey.CreateSurveyRequest
import com.nevmem.survey.data.response.survey.CreateSurveyResponse
import com.nevmem.survey.role.RoleModel
import com.nevmem.survey.routing.userId
import com.nevmem.survey.service.users.UsersService
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.post
import org.koin.ktor.ext.inject

fun Route.surveys() {
    val usersService by inject<UsersService>()
    val roleModel by inject<RoleModel>()

    authenticate {
        post("/create_survey") {
            try {
                val user = usersService.getUserById(userId())!!

                if (!roleModel.hasAccess(listOf(roleModel.roleById("survey.create")), user.roles)) {
                    throw IllegalStateException("Access to method denied (not enough roles)")
                }

                val request = call.receive<CreateSurveyRequest>()



            } catch (exception: Exception) {
                call.respond(CreateSurveyResponse.CreateSurveyError(exception.message ?: "Unknown error"))
            }
        }
    }
}
