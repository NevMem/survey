package com.nevmem.survey.fs

import io.ktor.http.content.MultiPartData
import java.io.File

interface FileSystemService {

    enum class FileType {
        CSV,
        TXT,
    }

    suspend fun saveFromMultiPart(data: MultiPartData): File

    suspend fun createFile(type: FileType): File
}
