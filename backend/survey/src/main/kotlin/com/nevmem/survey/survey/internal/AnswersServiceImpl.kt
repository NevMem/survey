package com.nevmem.survey.survey.internal

import com.nevmem.survey.commonQuestion.CommonQuestionEntity
import com.nevmem.survey.config.ConfigProvider
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
import com.nevmem.survey.survey.internal.saver.AnswerSaver
import com.nevmem.survey.survey.internal.saver.BunchAnswerSaver
import com.nevmem.survey.survey.internal.saver.HybridAnswersSaver
import com.nevmem.survey.survey.internal.saver.SimpleAnswerSaver
import com.nevmem.survey.survey.internal.util.typeOfCommonQuestion
import com.nevmem.survey.survey.internal.util.typeOfQuestion
import com.nevmem.survey.survey.internal.util.typeOfQuestionForAnswer
import com.nevmem.surveys.converters.MediaGalleryConverter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal class AnswersServiceImpl(
    private val mediaStorageService: MediaStorageService,
    private val backgroundScope: CoroutineScope,
    private val configProvider: ConfigProvider,
) : AnswersService, KoinComponent {

    private val surveysService by inject<SurveysService>()
    private val mediaGalleryConverter by inject<MediaGalleryConverter>()

    private val saver: AnswerSaver by lazy { HybridAnswersSaver(backgroundScope, configProvider) }

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

    override suspend fun answersStreamer(surveyId: String): Flow<SurveyAnswer> = flow {
        var index = 0L
        val batchSize = 128

        while (true) {
            val currentValues = transaction {
                SurveyAnswerDTO.find { SurveyAnswerTable.surveyId eq surveyId }
                    .limit(batchSize, index)
                    .map { it.entity() }
            }
            index += currentValues.size
            currentValues.forEach {
                emit(it)
            }
            if (currentValues.isEmpty()) {
                break
            }
        }
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
