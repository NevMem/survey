package com.nevmem.survey.network.api

import android.net.Uri
import com.nevmem.survey.data.answer.QuestionAnswer
import com.nevmem.survey.data.media.Media
import com.nevmem.survey.data.media.MediaGallery
import com.nevmem.survey.data.survey.Survey
import com.nevmem.survey.data.user.UserId

interface NetworkService {
    suspend fun loadSurvey(surveyId: String): Survey

    suspend fun sendSurvey(
        surveyId: String,
        uid: UserId,
        answers: List<QuestionAnswer>,
        mediaGallery: MediaGallery?,
    )

    suspend fun sendToken(uid: UserId, token: String?)

    suspend fun sendMedia(media: Uri): Media

    suspend fun createGallery(medias: List<Media>): MediaGallery
}
