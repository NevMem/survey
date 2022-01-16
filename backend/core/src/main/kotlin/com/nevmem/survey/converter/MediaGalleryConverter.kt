package com.nevmem.survey.converter

import com.nevmem.survey.data.media.MediaGallery
import com.nevmem.survey.media.MediaGalleryEntity
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MediaGalleryConverter : KoinComponent {

    private val mediaConverter by inject<MediaConverter>()

    private fun convert(mediaGalleryEntity: MediaGalleryEntity): MediaGallery {
        return MediaGallery(
            id = mediaGalleryEntity.id,
            gallery = mediaGalleryEntity.medias.map { mediaConverter(it) },
        )
    }

    operator fun invoke(mediaGalleryEntity: MediaGalleryEntity) = convert(mediaGalleryEntity)
}