package com.nevmem.survey.service.surveys

import com.nevmem.survey.data.survey.SurveyMetadata

interface SurveysMetadataAssembler {
    suspend fun assembleMetadata(surveyId: Long): SurveyMetadata
}
