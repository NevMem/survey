package com.nevmem.survey.data.request.media

import com.nevmem.survey.Exported
import kotlinx.serialization.Serializable

@Exported
@Serializable
data class GetGalleryRequest(
    val id: Long,
)
