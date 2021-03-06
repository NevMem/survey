package com.nevmem.survey.data.media

import com.nevmem.survey.Exported
import kotlinx.serialization.Serializable

@Serializable
@Exported
data class Media(
    val id: Long,
    val filename: String,
    val url: String,
    val bucketName: String,
)
