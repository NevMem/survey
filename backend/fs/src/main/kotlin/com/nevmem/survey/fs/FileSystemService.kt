package com.nevmem.survey.fs

import io.ktor.http.content.MultiPartData
import java.io.File

interface FileSystemService {

    enum class FileType(val ext: String) {
        CSV("csv"),
        TXT("txt"),
        JPG("jpg"),
        PNG("png"),
    }

    suspend fun saveFromMultiPart(data: MultiPartData): File

    suspend fun createFile(type: FileType): File
}
