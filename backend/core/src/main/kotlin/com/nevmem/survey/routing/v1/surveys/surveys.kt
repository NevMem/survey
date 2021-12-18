package com.nevmem.survey.routing.v1.surveys

import com.nevmem.survey.converter.SurveysConverter
import com.nevmem.survey.data.question.Question
import com.nevmem.survey.data.request.survey.ActivateSurveyRequest
import com.nevmem.survey.data.request.survey.CreateSurveyRequest
import com.nevmem.survey.data.request.survey.DeleteSurveyRequest
import com.nevmem.survey.data.response.survey.ActiveSurveyResponse
import com.nevmem.survey.data.response.survey.AllSurveysResponse
import com.nevmem.survey.data.response.survey.CreateSurveyResponse
import com.nevmem.survey.role.RoleModel
import com.nevmem.survey.routing.checkRoles
import com.nevmem.survey.routing.userId
import com.nevmem.survey.service.surveys.SurveysService
import com.nevmem.survey.service.surveys.data.CommonQuestionEntity
import com.nevmem.survey.service.surveys.data.QuestionEntity
import com.nevmem.survey.service.users.UsersService
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import org.koin.ktor.ext.inject

fun Route.surveys() {
    val usersService by inject<UsersService>()
    val roleModel by inject<RoleModel>()
    val surveysService by inject<SurveysService>()
    val surveysConverter by inject<SurveysConverter>()

    get("/active_survey") {
        call.respond(ActiveSurveyResponse(surveysService.currentActiveSurvey()?.let { surveysConverter.convertSurvey(it) }))
    }

    authenticate {
        post("/create_survey") {
            try {
                val user = usersService.getUserById(userId())!!

                if (!roleModel.hasAccess(listOf(roleModel.roleById("survey.create")), user.roles)) {
                    throw IllegalStateException("Access to method denied (not enough roles)")
                }

                val request = call.receive<CreateSurveyRequest>()

                val survey = surveysService.createSurvey(
                    name = request.name,
                    questions = request.questions.map {
                        when (it) {
                            is Question.TextQuestion -> QuestionEntity.TextQuestionEntity(
                                id = 0,
                                title = it.title,
                                maxLength = it.maxLength,
                            )
                            is Question.StarsQuestion -> QuestionEntity.StarsQuestionEntity(
                                id = 0,
                                title = it.title,
                                stars = it.stars,
                            )
                            is Question.RatingQuestion -> QuestionEntity.RatingQuestionEntity(
                                id = 0,
                                title = it.title,
                                min = it.min,
                                max = it.max,
                            )
                        }
                    },
                    commonQuestion = request.commonQuestions.map {
                        CommonQuestionEntity(it.id)
                    }
                )

                call.respond(CreateSurveyResponse.CreateSurveySuccess(surveysConverter.convertSurvey(survey)))
            } catch (exception: Exception) {
                call.respond(HttpStatusCode.ExpectationFailed, CreateSurveyResponse.CreateSurveyError(exception.message ?: "Unknown error"))
            }
        }

        post("/activate_survey") {
            checkRoles(roleModel, usersService, listOf("survey.activate"))
            val request = call.receive<ActivateSurveyRequest>()
            surveysService.activateSurvey(request.surveyId)
            call.respond(HttpStatusCode.OK)
        }

        post("/delete_survey") {
            checkRoles(roleModel, usersService, listOf("survey.manager"))
            val request = call.receive<DeleteSurveyRequest>()
            surveysService.deleteSurvey(request.surveyId)
            call.respond(HttpStatusCode.OK)
        }

        get("/surveys") {
            call.respond(
                AllSurveysResponse(
                    surveysService.allSurveys().map { surveysConverter.convertSurvey(it) }
                )
            )
        }
    }
}
