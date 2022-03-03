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
            val answers = answersService.answers(survey.surveyId)
            tasksService.appendLog(task, "Loaded ${answers.count()} answers")

            val file = fs.createFile(FileSystemService.FileType.CSV)
            tasksService.appendLog(task, "Created temporary file ${file.name}")

            tasksService.appendLog(task, "Exporting answers")

            withContext(Dispatchers.IO) {
                val writer = file.bufferedWriter()

                val headerLine =
                    listOf("uuid") +
                        survey.commonQuestions.map { it.id } +
                        survey.questions.map {
                            when (it) {
                                is QuestionEntity.StarsQuestionEntity -> it.title
                                is QuestionEntity.TextQuestionEntity -> it.title
                                is QuestionEntity.RatingQuestionEntity -> it.title
                            }
                        }

                writer.write(headerLine.joinToString(",") + "\n")
                answers.forEach {
                    writer.write(it.csvLine() + "\n")
                    tasksService.appendLog(task, "Written line: " + it.csvLine())
                }
                writer.flush()
                writer.close()
            }

            tasksService.appendLog(task, "Uploading result file to store")
            val media = mediaStorageService.uploadFileToMediaStorage(file)
            tasksService.appendLog(task, "Media file has been successfully uploaded")

            tasksService.attachOutput(task, media)

            tasksService.appendLog(task, "Done")
            tasksService.atomicallyTransferToState(task, TaskStateEntity.Executing, TaskStateEntity.Success)
        } catch (exception: Exception) {
            tasksService.appendLog(task, "Exception occurred ${exception.message}")
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
            }
        }

        return (listOf(uid.uuid) + answers).joinToString(",") { it.toString() }
    }
}
