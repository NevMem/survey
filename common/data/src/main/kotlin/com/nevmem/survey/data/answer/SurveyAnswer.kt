package com.nevmem.survey.data.answer

import com.nevmem.survey.data.media.MediaGallery
import com.nevmem.survey.data.user.UserId
import kotlinx.serialization.Serializable

@Serializable
data class SurveyAnswer(
    val uid: UserId,
    val surveyId: String,
    val answers: List<QuestionAnswer>,
    val gallery: MediaGallery?,
)
