package com.nevmem.survey.data.request.media

import com.nevmem.survey.data.media.Media
import kotlinx.serialization.Serializable

@Serializable
data class CreateGalleryRequest(
    val gallery: List<Media>,
)
