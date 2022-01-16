package com.nevmem.survey.converter

import com.nevmem.survey.data.media.Media
import com.nevmem.survey.media.MediaEntity

class MediaConverter {
    private fun convertMedia(entity: MediaEntity): Media {
        return Media(entity.id, entity.filename, entity.url, entity.bucketName)
    }

    operator fun invoke(entity: MediaEntity) = convertMedia(entity)
}