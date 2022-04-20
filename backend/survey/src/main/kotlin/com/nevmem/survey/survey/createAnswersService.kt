package com.nevmem.survey.survey

import com.nevmem.survey.media.MediaStorageService
import com.nevmem.survey.survey.internal.AnswersServiceImpl
import com.nevmem.survey.survey.internal.QuestionAnswerTable
import com.nevmem.survey.survey.internal.SurveyAnswerTable
import org.jetbrains.exposed.sql.Table

fun createAnswersService(
    mediaStorageService: MediaStorageService,
): AnswersService = AnswersServiceImpl(mediaStorageService)

fun answersTables(): List<Table> = listOf(
    QuestionAnswerTable,
    SurveyAnswerTable,
)
