package com.nevmem.survey.routing.v1.answer

import com.nevmem.survey.data.request.answer.PublishAnswerRequest
import com.nevmem.survey.data.response.answer.PublishAnswerResponse
import com.nevmem.survey.service.answer.AnswersService
import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.post
import io.ktor.routing.route
import org.koin.ktor.ext.inject

private fun Route.answersImpl() {
    val answersService by inject<AnswersService>()

    post("/publish") {
        val request = call.receive<PublishAnswerRequest>()
        answersService.publishAnswer(request.answer, request.publisherId)
        call.respond(PublishAnswerResponse(request.answer.surveyId))
    }
}

fun Route.answers() {
    route("/answers") {
        answersImpl()
    }
}
