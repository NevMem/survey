package com.nevmem.survey.routing.v2

import com.nevmem.survey.commonQuestion.CommonQuestionEntity
import com.nevmem.survey.data.question.Question
import com.nevmem.survey.data.request.survey.CreateSurveyRequest
import com.nevmem.survey.data.request.survey.DeleteSurveyRequest
import com.nevmem.survey.data.request.survey.GetSurveyRequest
import com.nevmem.survey.data.request.survey.JoinSurveyRequest
import com.nevmem.survey.data.request.survey.GetSurveysRequest
import com.nevmem.survey.data.request.survey.LoadSurveyMetadataRequest
import com.nevmem.survey.data.response.survey.CreateSurveyResponse
import com.nevmem.survey.data.response.survey.GetSurveyResponse
import com.nevmem.survey.data.response.survey.JoinSurveyResponse
import com.nevmem.survey.data.response.survey.GetSurveysResponse
import com.nevmem.survey.data.response.survey.LoadSurveyMetadataResponse
import com.nevmem.survey.exception.AccessDeniedException
import com.nevmem.survey.exception.NotFoundException
import com.nevmem.survey.question.QuestionEntity
import com.nevmem.survey.question.QuestionVariantEntity
import com.nevmem.survey.role.RoleModel
import com.nevmem.survey.routing.toRoles
import com.nevmem.survey.routing.userId
import com.nevmem.survey.survey.ProjectsService
import com.nevmem.survey.survey.SurveysMetadataAssembler
import com.nevmem.survey.survey.SurveysService
import com.nevmem.survey.users.UsersService
import com.nevmem.surveys.converters.SurveysConverter
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.post
import io.ktor.routing.route
import org.koin.ktor.ext.inject

private fun Route.surveysImpl() {
    val usersService by inject<UsersService>()
    val roleModel by inject<RoleModel>()
    val surveysService by inject<SurveysService>()
    val surveysConverter by inject<SurveysConverter>()
    val surveysMetadataAssembler by inject<SurveysMetadataAssembler>()
    val projectsService by inject<ProjectsService>()

    post("/join") {
        val request = call.receive<JoinSurveyRequest>()
        val survey = surveysService.survey(request.surveyId) ?: throw NotFoundException()
        call.respond(
            JoinSurveyResponse(
                surveysConverter.convertSurvey(
                    survey
                )
            )
        )
    }

    authenticate {
        post("/get") {
            val request = call.receive<GetSurveyRequest>()
            val survey = surveysService.surveyById(request.id) ?: throw NotFoundException()
            call.respond(
                GetSurveyResponse(
                    surveysConverter(survey)
                )
            )
        }

        post("/create_survey") {
            try {
                val user = usersService.getUserById(userId())!!

                val request = call.receive<CreateSurveyRequest>()

                val project = projectsService.get(request.projectId) ?: throw NotFoundException("Project with id ${request.projectId} not found")
                val userRoles = projectsService.getRolesInProject(project, user)

                if (!roleModel.hasAccess(listOf("survey.creator").toRoles(roleModel), userRoles)) {
                    throw IllegalStateException("Access to method denied (not enough roles)")
                }

                val survey = surveysService.createSurvey(
                    projectId = request.projectId,
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
                            is Question.RadioQuestion -> QuestionEntity.RadioQuestionEntity(
                                id = 0,
                                title = it.title,
                                variants = it.variants.map { variant -> QuestionVariantEntity(variant.id, variant.variant) },
                            )
                        }
                    },
                    commonQuestion = request.commonQuestions.map {
                        CommonQuestionEntity(it.id)
                    },
                    answerCoolDown = request.answerCoolDown,
                )

                call.respond<CreateSurveyResponse>(CreateSurveyResponse.CreateSurveySuccess(surveysConverter.convertSurvey(survey)))
            } catch (exception: Exception) {
                call.respond<CreateSurveyResponse>(HttpStatusCode.ExpectationFailed, CreateSurveyResponse.CreateSurveyError(exception.message ?: "Unknown error"))
            }
        }

        post("/delete_survey") {
            val request = call.receive<DeleteSurveyRequest>()

            val survey = surveysService.surveyById(request.surveyId) ?: throw NotFoundException("Survey with id ${request.surveyId} not found")
            val project = projectsService.get(survey.projectId)!!

            val user = usersService.getUserById(userId())!!
            val userRoles = projectsService.getRolesInProject(project, user)

            if (!roleModel.hasAccess(listOf("survey.creator").toRoles(roleModel), userRoles)) {
                throw IllegalStateException("Access to method denied (not enough roles)")
            }

            surveysService.deleteSurvey(request.surveyId)
            call.respond(HttpStatusCode.OK)
        }

        post("/surveys") {
            val request = call.receive<GetSurveysRequest>()

            val user = usersService.getUserById(userId())!!
            val project = projectsService.get(request.projectId) ?: throw NotFoundException("Project with id ${request.projectId} not found")

            if (!projectsService.isUserInvitedToProject(project, user)) {
                throw AccessDeniedException()
            }

            call.respond(
                GetSurveysResponse(
                    projectId = request.projectId,
                    surveys = surveysService.surveysInProject(request.projectId).map { surveysConverter.convertSurvey(it) }
                )
            )
        }

        post("/metadata") {
            val request = call.receive<LoadSurveyMetadataRequest>()

            val survey = surveysService.surveyById(request.surveyId) ?: throw NotFoundException("Survey with id ${request.surveyId} not found")
            val project = projectsService.get(survey.projectId) ?: throw NotFoundException("Project with id ${survey.projectId} not found")
            val user = usersService.getUserById(userId())!!
            val roles = projectsService.getRolesInProject(project, user)

            if (!roleModel.hasAccess(listOf("survey.observer").toRoles(roleModel), roles)) {
                throw AccessDeniedException()
            }

            call.respond(LoadSurveyMetadataResponse(surveysMetadataAssembler.assembleMetadata(request.surveyId)))
        }
    }
}

fun Route.surveys() {
    route("/survey") {
        surveysImpl()
    }
}
