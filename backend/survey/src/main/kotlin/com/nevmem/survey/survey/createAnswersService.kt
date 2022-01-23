package com.nevmem.survey.survey

import com.nevmem.survey.survey.internal.AnswersServiceImpl
import com.nevmem.survey.survey.internal.QuestionAnswerTable
import com.nevmem.survey.survey.internal.SurveyAnswerTable
import org.jetbrains.exposed.sql.Table

fun createAnswersService(): AnswersService = AnswersServiceImpl()

fun answersTables(): List<Table> = listOf(
    QuestionAnswerTable,
    SurveyAnswerTable,
)
