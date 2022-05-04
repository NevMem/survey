package com.nevmem.survey.fs.internal

import com.nevmem.survey.RandomStringGenerator
import com.nevmem.survey.fs.FileSystemService
import com.nevmem.survey.fs.FolderHelper
import io.ktor.http.content.MultiPartData
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.http.content.streamProvider
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

private class FolderHelperImpl(
    override val file: File,
) : FolderHelper {
    override fun createOrGetFolder(name: String): FolderHelper {
        val folder = File("${file.absolutePath}/$name")
        if (!folder.exists()) {
            folder.mkdirs()
        }
        return FolderHelperImpl(folder)
    }

    override fun createFile(name: String): File {
        return File("${file.absolutePath}/$name")
    }
}

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

    override suspend fun createFile(type: FileSystemService.FileType): File {
        return File("$tmpDirPath/${RandomStringGenerator.randomString(32)}.${type.ext}")
    }

    override suspend fun createFolder(): FolderHelper {
        val file = File("$tmpDirPath/${RandomStringGenerator.randomString(32)}/")
        file.mkdirs()
        return FolderHelperImpl(file)
    }

    override suspend fun zipIt(folder: File): File {
        assert(folder.isDirectory)
        val file = File("$tmpDirPath/${folder.name}.zip")
        val fos = FileOutputStream(file)
        val zipOut = ZipOutputStream(fos)

        zipFile(folder, folder.name, zipOut)
        zipOut.close()
        fos.close()
        return file
    }

    private fun zipFile(fileToZip: File, fileName: String, zipOut: ZipOutputStream) {
        if (fileToZip.isHidden) {
            return
        }
        if (fileToZip.isDirectory) {
            if (fileName.endsWith("/")) {
                zipOut.putNextEntry(ZipEntry(fileName))
                zipOut.closeEntry()
            } else {
                zipOut.putNextEntry(ZipEntry("$fileName/"))
                zipOut.closeEntry()
            }
            val children = fileToZip.listFiles()
            for (childFile in children) {
                zipFile(childFile, fileName + "/" + childFile.name, zipOut)
            }
            return
        }
        val fis = FileInputStream(fileToZip)
        val zipEntry = ZipEntry(fileName)
        zipOut.putNextEntry(zipEntry)
        val bytes = ByteArray(1024)
        var length: Int
        while (fis.read(bytes).also { length = it } >= 0) {
            zipOut.write(bytes, 0, length)
        }
        fis.close()
    }
}
