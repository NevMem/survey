package com.nevmem.survey.survey.internal

import com.nevmem.survey.commonQuestion.CommonQuestionEntity
import com.nevmem.survey.data.answer.QuestionAnswer
import com.nevmem.survey.data.answer.SurveyAnswer
import com.nevmem.survey.data.user.UserId
import com.nevmem.survey.question.QuestionEntity
import com.nevmem.survey.survey.AlreadyPublishedAnswerException
import com.nevmem.survey.survey.AnswersService
import com.nevmem.survey.survey.SurveyAnswerInconsistencyException
import com.nevmem.survey.survey.SurveyNotFoundException
import com.nevmem.survey.survey.SurveysService
import com.nevmem.survey.survey.UnknownCommonQuestionException
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal class AnswersServiceImpl : AnswersService, KoinComponent {

    private enum class QuestionType {
        Text,
        Rating,
        Stars,
        Radio,
    }

    private val surveysService by inject<SurveysService>()

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

        val maxTimestamp = transaction {
            SurveyAnswerDTO.find {
                (SurveyAnswerTable.publisherId eq answer.uid.uuid) and
                    (SurveyAnswerTable.surveyId eq answer.surveyId)
            }.maxByOrNull { SurveyAnswerTable.timestamp }?.timestamp
        }
        if (maxTimestamp != null && maxTimestamp + survey.answerCoolDown >= System.currentTimeMillis()) {
            throw AlreadyPublishedAnswerException()
        }

        // Ok. Simple validation passed. Storing answer to storage

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

    override suspend fun answers(surveyId: String): List<SurveyAnswer> = transaction {
        val dtos = SurveyAnswerDTO.find { SurveyAnswerTable.surveyId eq surveyId }
        dtos.map { it.entity() }
    }

    override suspend fun getAnswers(surveyId: String): String = transaction {
        val dto = SurveyAnswerDTO.find { SurveyAnswerTable.surveyId eq surveyId }

        @Serializable
        data class DebugQuestionAnswer(val type: SurveyAnswerType, val jsonAnswer: String)
        @Serializable
        data class DebugSurveyAnswer(val publisherId: String, val answers: List<DebugQuestionAnswer>)

        val result = dto.map {
            DebugSurveyAnswer(
                publisherId = it.publisherId,
                answers = it.answers.map { answerDto ->
                    DebugQuestionAnswer(answerDto.type, answerDto.jsonAnswer)
                }
            )
        }

        Json.encodeToString(result)
    }

    override suspend fun getAnswersCount(surveyId: String): Long {
        return transaction {
            SurveyAnswerDTO.find {
                SurveyAnswerTable.surveyId eq surveyId
            }.count()
        }
    }

    private fun typeOfCommonQuestion(question: CommonQuestionEntity): QuestionType {
        return when (question.id) {
            "age" -> QuestionType.Rating
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

    private fun SurveyAnswerDTO.entity(): SurveyAnswer {
        return SurveyAnswer(
            surveyId = this.surveyId,
            gallery = null,
            answers = this.answers.map { it.entity() },
            uid = UserId(this.publisherId),
        )
    }

    private fun QuestionAnswerDTO.entity(): QuestionAnswer {
        return Json.decodeFromString(QuestionAnswer.serializer(), this.jsonAnswer)
    }
}
