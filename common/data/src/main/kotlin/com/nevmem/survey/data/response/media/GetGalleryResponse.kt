package com.nevmem.survey.data.response.media

import com.nevmem.survey.Exported
import com.nevmem.survey.data.media.MediaGallery
import kotlinx.serialization.Serializable

@Exported
@Serializable
data class GetGalleryResponse(
    val gallery: MediaGallery,
)
