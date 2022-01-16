package com.nevmem.survey.data.response.media

import com.nevmem.survey.data.media.MediaGallery
import kotlinx.serialization.Serializable

@Serializable
data class CreateGalleryResponse(
    val gallery: MediaGallery,
)
