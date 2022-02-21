package com.nevmem.survey.survey

import com.nevmem.survey.data.survey.SurveyMetadata

interface SurveysMetadataAssembler {
    suspend fun assembleMetadata(surveyId: Long): SurveyMetadata
}
