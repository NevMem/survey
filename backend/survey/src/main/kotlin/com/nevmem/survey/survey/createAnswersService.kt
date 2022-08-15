package com.nevmem.survey.survey

import com.nevmem.survey.config.ConfigProvider
import com.nevmem.survey.media.MediaStorageService
import com.nevmem.survey.survey.internal.AnswersServiceImpl
import com.nevmem.survey.survey.internal.QuestionAnswerTable
import com.nevmem.survey.survey.internal.SurveyAnswerTable
import kotlinx.coroutines.CoroutineScope
import org.jetbrains.exposed.sql.Table

fun createAnswersService(
    mediaStorageService: MediaStorageService,
    backgroundScope: CoroutineScope,
    configProvider: ConfigProvider,
): AnswersService = AnswersServiceImpl(mediaStorageService, backgroundScope, configProvider)

fun answersTables(): List<Table> = listOf(
    QuestionAnswerTable,
    SurveyAnswerTable,
)
