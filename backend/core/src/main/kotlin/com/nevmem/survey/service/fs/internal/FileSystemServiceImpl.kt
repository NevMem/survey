package com.nevmem.survey.service.fs.internal

import com.nevmem.survey.RandomStringGenerator
import com.nevmem.survey.service.fs.FileSystemService
import io.ktor.http.content.MultiPartData
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.http.content.streamProvider
import java.io.File

internal class FileSystemServiceImpl : FileSystemService {
    private val tmpDirPath = "./file_system_tmp_dir"

    init {
        val tmpDir = File(tmpDirPath)
        if (!tmpDir.exists()) {
            check(File(tmpDirPath).mkdir()) { "Failed to create tmp dir for FileSystemServiceImpl" }
        }
    }

    override suspend fun saveFromMultiPart(data: MultiPartData): File {
        val tmpFilename = RandomStringGenerator.randomString(32)
        var file = File("$tmpDirPath/$tmpFilename")
        var extension: String? = null
        data.forEachPart { part ->
            when (part) {
                is PartData.FileItem -> {
                    extension = part.originalFileName?.let { File(it).extension }
                    val fileBytes = part.streamProvider().readBytes()
                    file.writeBytes(fileBytes)
                }
            }
        }
        if (extension != null) {
            val renameToFile = File("$tmpDirPath/$tmpFilename.$extension")
            if (file.renameTo(renameToFile)) {
                file = renameToFile
            }
        }

        return file
    }
}
