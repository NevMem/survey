package com.nevmem.survey.survey.internal

import com.nevmem.survey.commonQuestion.CommonQuestionEntity
import com.nevmem.survey.data.answer.QuestionAnswer
import com.nevmem.survey.data.answer.SurveyAnswer
import com.nevmem.survey.data.survey.Survey
import com.nevmem.survey.data.user.UserId
import com.nevmem.survey.media.MediaStorageService
import com.nevmem.survey.question.QuestionEntity
import com.nevmem.survey.survey.AlreadyPublishedAnswerException
import com.nevmem.survey.survey.AnswersService
import com.nevmem.survey.survey.SurveyAnswerInconsistencyException
import com.nevmem.survey.survey.SurveyNotFoundException
import com.nevmem.survey.survey.SurveysService
import com.nevmem.survey.survey.UnknownCommonQuestionException
import com.nevmem.surveys.converters.MediaGalleryConverter
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private interface AnswerSaver {
    fun save(answer: SurveyAnswer)
}

private class SimpleAnswerSaver : AnswerSaver {
    override fun save(answer: SurveyAnswer) {
        transaction {
            val surveyAnswer = SurveyAnswerDTO.new {
                this.publisherId = answer.uid.uuid
                this.surveyId = answer.surveyId
                this.mediaGalleryId = answer.gallery?.id
                this.timestamp = System.currentTimeMillis()
            }

            answer.answers.forEach { actualAnswer ->
                val type = when (typeOfQuestionForAnswer(actualAnswer)) {
                    QuestionType.Rating -> SurveyAnswerType.Rating
                    QuestionType.Stars -> SurveyAnswerType.Stars
                    QuestionType.Text -> SurveyAnswerType.Text
                    QuestionType.Radio -> SurveyAnswerType.Radio
                }

                QuestionAnswerDTO.new {
                    this.surveyAnswer = surveyAnswer.id
                    this.type = type
                    this.jsonAnswer = Json.encodeToString(QuestionAnswer.serializer(), actualAnswer)
                }
            }
        }
    }
}

private class BunchAnswerSaver : AnswerSaver {
    override fun save(answer: SurveyAnswer) {
        TODO("Not yet implemented")
    }
}

private enum class QuestionType {
    Text,
    Rating,
    Stars,
    Radio,
}

internal class AnswersServiceImpl(
    private val mediaStorageService: MediaStorageService,
) : AnswersService, KoinComponent {

    private val surveysService by inject<SurveysService>()
    private val mediaGalleryConverter by inject<MediaGalleryConverter>()

    private val saver: AnswerSaver by lazy {
        SimpleAnswerSaver()
    }

    override suspend fun publishAnswer(answer: SurveyAnswer) {
        val survey = surveysService.survey(answer.surveyId) ?: throw SurveyNotFoundException()
        if (survey.questions.size + survey.commonQuestions.size != answer.answers.size) {
            throw SurveyAnswerInconsistencyException()
        }

        val allQuestions = survey.commonQuestions + survey.questions
        for (index in allQuestions.indices) {
            val question = allQuestions[index]
            if (question is QuestionEntity) {
                if (typeOfQuestion(question) != typeOfQuestionForAnswer(answer.answers[index])) {
                    throw SurveyAnswerInconsistencyException()
                }
            } else if (question is CommonQuestionEntity) {
                if (typeOfCommonQuestion(question) != typeOfQuestionForAnswer(answer.answers[index])) {
                    throw SurveyAnswerInconsistencyException()
                }
            } else {
                throw IllegalStateException("Some shit happened $question")
            }
        }

        if (survey.answerCoolDown != Survey.IGNORE_COOL_DOWN) {
            if (survey.answerCoolDown == Survey.SURVEY_COOL_DOWN_ONLY_ONCE) {
                val hasAnswer = transaction {
                    SurveyAnswerDTO.find {
                        (SurveyAnswerTable.publisherId eq answer.uid.uuid) and
                            (SurveyAnswerTable.surveyId eq answer.surveyId)
                    }.firstOrNull() != null
                }
                if (hasAnswer) {
                    throw AlreadyPublishedAnswerException()
                }
            } else {
                val answeredCount = transaction {
                    SurveyAnswerDTO.count(
                        Op.build {
                            (SurveyAnswerTable.publisherId eq answer.uid.uuid) and
                                (SurveyAnswerTable.surveyId eq answer.surveyId) and
                                (SurveyAnswerTable.timestamp greater System.currentTimeMillis() - survey.answerCoolDown)
                        }
                    )
                }
                if (answeredCount > 0) {
                    throw AlreadyPublishedAnswerException()
                }
            }
        }

        // Ok. Simple validation passed. Storing answer to storage

        saver.save(answer)
    }

    override suspend fun answers(surveyId: String): List<SurveyAnswer> = transaction {
        SurveyAnswerDTO.find { SurveyAnswerTable.surveyId eq surveyId }.map { it.entity() }
    }

    override suspend fun getAnswersCount(surveyId: String): Long {
        return transaction {
            SurveyAnswerDTO.find {
                SurveyAnswerTable.surveyId eq surveyId
            }.count()
        }
    }

    private fun SurveyAnswerDTO.entity(): SurveyAnswer {
        return SurveyAnswer(
            surveyId = this.surveyId,
            timestamp = this.timestamp,
            gallery = this.mediaGalleryId ?.let { runBlocking { mediaGalleryConverter(mediaStorageService.mediaGallery(it)!!) } },
            answers = this.answers.map { it.entity() },
            uid = UserId(this.publisherId),
        )
    }

    private fun QuestionAnswerDTO.entity(): QuestionAnswer {
        return Json.decodeFromString(QuestionAnswer.serializer(), this.jsonAnswer)
    }
}

private fun typeOfCommonQuestion(question: CommonQuestionEntity): QuestionType {
    return when (question.id) {
        "age" -> QuestionType.Rating
        "school_name" -> QuestionType.Text
        "region" -> QuestionType.Text
        "grade" -> QuestionType.Rating
        else -> throw UnknownCommonQuestionException(question.id)
    }
}

private fun typeOfQuestion(question: QuestionEntity): QuestionType {
    return when (question) {
        is QuestionEntity.RatingQuestionEntity -> QuestionType.Rating
        is QuestionEntity.TextQuestionEntity -> QuestionType.Text
        is QuestionEntity.StarsQuestionEntity -> QuestionType.Stars
        is QuestionEntity.RadioQuestionEntity -> QuestionType.Radio
    }
}

private fun typeOfQuestionForAnswer(answer: QuestionAnswer): QuestionType {
    return when (answer) {
        is QuestionAnswer.TextQuestionAnswer -> QuestionType.Text
        is QuestionAnswer.StarsQuestionAnswer -> QuestionType.Stars
        is QuestionAnswer.RatingQuestionAnswer -> QuestionType.Rating
        is QuestionAnswer.RadioQuestionAnswer -> QuestionType.Radio
    }
}
