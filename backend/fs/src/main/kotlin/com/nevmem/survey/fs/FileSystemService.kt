package com.nevmem.survey.fs

import io.ktor.http.content.MultiPartData
import java.io.File

interface FileSystemService {
    suspend fun saveFromMultiPart(data: MultiPartData): File
}
