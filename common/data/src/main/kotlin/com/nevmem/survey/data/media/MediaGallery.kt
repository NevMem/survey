package com.nevmem.survey.data.media

import com.nevmem.survey.Exported
import kotlinx.serialization.Serializable

@Serializable
@Exported
data class MediaGallery(
    val id: Long,
    val gallery: List<Media>,
)
