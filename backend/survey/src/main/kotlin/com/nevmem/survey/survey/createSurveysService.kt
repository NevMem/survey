package com.nevmem.survey.survey

import com.nevmem.survey.survey.internal.CommonQuestionsTable
import com.nevmem.survey.survey.internal.QuestionsTable
import com.nevmem.survey.survey.internal.SurveysServiceImpl
import com.nevmem.survey.survey.internal.SurveysTable
import org.jetbrains.exposed.sql.Table

fun createSurveysService(): SurveysService = SurveysServiceImpl()

fun surveysTables(): List<Table> = listOf(
    SurveysTable,
    QuestionsTable,
    CommonQuestionsTable,
)
