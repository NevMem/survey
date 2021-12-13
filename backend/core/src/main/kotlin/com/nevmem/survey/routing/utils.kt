package com.nevmem.survey.routing

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.auth.jwt.JWTPrincipal
import io.ktor.auth.principal
import io.ktor.util.pipeline.PipelineContext

fun PipelineContext<*, ApplicationCall>.userId() = call.principal<JWTPrincipal>()!!["user_id"]!!.toLong()
