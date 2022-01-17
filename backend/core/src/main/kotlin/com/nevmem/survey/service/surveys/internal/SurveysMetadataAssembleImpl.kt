package com.nevmem.survey.service.surveys.internal

import com.nevmem.survey.data.survey.SurveyMetadata
import com.nevmem.survey.service.answer.AnswersService
import com.nevmem.survey.service.surveys.SurveysMetadataAssembler
import com.nevmem.survey.service.surveys.SurveysService
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SurveysMetadataAssembleImpl : SurveysMetadataAssembler, KoinComponent {
    private val surveysService by inject<SurveysService>()
    private val answersService by inject<AnswersService>()

    override suspend fun assembleMetadata(surveyId: Long): SurveyMetadata {
        val survey = surveysService.surveyById(surveyId) ?: throw IllegalStateException("Survey with id $surveyId not found")
        return SurveyMetadata(
            answersCount = answersService.getAnswersCount(survey.surveyId),
            filesCount = -1, // Not supported nows
        )
    }
}