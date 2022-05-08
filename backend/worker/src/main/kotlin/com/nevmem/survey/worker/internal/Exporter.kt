package com.nevmem.survey.worker.internal

import com.nevmem.survey.data.answer.QuestionAnswer
import com.nevmem.survey.data.answer.SurveyAnswer
import com.nevmem.survey.fs.FileSystemService
import com.nevmem.survey.media.MediaStorageService
import com.nevmem.survey.question.QuestionEntity
import com.nevmem.survey.survey.AnswersService
import com.nevmem.survey.survey.SurveysService
import com.nevmem.survey.task.ExportDataTaskEntity
import com.nevmem.survey.task.TaskService
import com.nevmem.survey.task.TaskStateEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class Exporter : KoinComponent {
    private val fs by inject<FileSystemService>()
    private val surveysService by inject<SurveysService>()
    private val answersService by inject<AnswersService>()
    private val tasksService by inject<TaskService>()
    private val mediaStorageService by inject<MediaStorageService>()

    suspend fun runExportTask(task: ExportDataTaskEntity) {
        try {
            val survey = surveysService.surveyById(task.surveyId) ?: throw IllegalStateException()
            tasksService.appendLog(task, "Loading answers")
            val answers = mutableListOf<SurveyAnswer>()
            var loaded = 0
            answersService.answersStreamer(survey.surveyId).collect { answer ->
                answers.add(answer)
                loaded += 1
                if (loaded % 1000 == 0) {
                    tasksService.appendLog(task, "Loaded $loaded answers")
                }
            }
            tasksService.appendLog(task, "Loaded ${answers.count()} answers")

            val file = fs.createFile(FileSystemService.FileType.CSV)
            tasksService.appendLog(task, "Created temporary file ${file.name}")

            tasksService.appendLog(task, "Exporting answers")

            withContext(Dispatchers.IO) {
                val writer = file.bufferedWriter()

                val headerLine =
                    listOf("uuid", "timestamp") +
                        survey.commonQuestions.map { it.id } +
                        survey.questions.map {
                            when (it) {
                                is QuestionEntity.StarsQuestionEntity -> it.title
                                is QuestionEntity.TextQuestionEntity -> it.title
                                is QuestionEntity.RatingQuestionEntity -> it.title
                                is QuestionEntity.RadioQuestionEntity -> it.title
                            }
                        } + listOf("mediaGalleryId")

                writer.write(headerLine.joinToString(",") + "\n")
                answers.forEach {
                    writer.write(it.csvLine() + "\n")
                }
                writer.flush()
                writer.close()
            }

            tasksService.appendLog(task, "Uploading result file to store")
            val media = mediaStorageService.uploadFileToMediaStorage(file)
            tasksService.appendLog(task, "Media file has been successfully uploaded")

            val folderHelper = fs.createFolder()
            tasksService.appendLog(task, "Downloading media files")
            answers.groupBy { it.uid }.forEach { (uid, answers) ->
                val uidFolder = folderHelper.createOrGetFolder(uid.uuid)
                tasksService.appendLog(task, "Working with answers from user with uid: ${uid.uuid}")
                answers.forEach { answer ->
                    val entities =
                        answer.gallery?.gallery?.mapNotNull { mediaStorageService.mediaById(it.id) }
                            ?: return@forEach

                    val folder = uidFolder.createOrGetFolder("${answer.timestamp}")
                    entities.forEach { entity ->
                        val mediaFile = folder.createFile(entity.filename)
                        mediaStorageService.downloadToFile(mediaFile, entity)
                    }
                }
            }
            tasksService.appendLog(task, "Downloaded media files")

            tasksService.appendLog(task, "Zipping folder")
            val zipFile = fs.zipIt(folderHelper.file)
            tasksService.appendLog(task, "Folder successfully zipped")

            tasksService.appendLog(task, "Uploading zip to storage")
            val zipFileMedia = mediaStorageService.uploadFileToMediaStorage(zipFile)
            tasksService.appendLog(task, "Uploaded zip to storage")

            tasksService.attachOutput(task, media)
            tasksService.attachOutput(task, zipFileMedia)

            tasksService.appendLog(task, "Done")
            tasksService.atomicallyTransferToState(task, TaskStateEntity.Executing, TaskStateEntity.Success)
        } catch (exception: Exception) {
            tasksService.appendLog(task, "Exception occurred ${exception.message}")
            exception.stackTrace.forEach {
                tasksService.appendLog(task, it.toString())
            }
            tasksService.atomicallyTransferToState(task, TaskStateEntity.Executing, TaskStateEntity.Error)
        }
    }

    private fun SurveyAnswer.csvLine(): String {
        val answers = answers.map { answer ->
            when (answer) {
                is QuestionAnswer.RatingQuestionAnswer -> {
                    answer.number
                }
                is QuestionAnswer.TextQuestionAnswer -> {
                    answer.text
                }
                is QuestionAnswer.StarsQuestionAnswer -> {
                    answer.stars
                }
                is QuestionAnswer.RadioQuestionAnswer -> {
                    answer.id
                }
            }
        }

        return (listOf(uid.uuid, timestamp) + answers + (gallery?.id ?: "")).joinToString(",") { it.toString() }
    }
}
