package com.nevmem.survey.survey.internal.saver

import com.nevmem.survey.data.answer.QuestionAnswer
import com.nevmem.survey.data.answer.SurveyAnswer
import com.nevmem.survey.survey.internal.QuestionAnswerDTO
import com.nevmem.survey.survey.internal.SurveyAnswerDTO
import com.nevmem.survey.survey.internal.SurveyAnswerType
import com.nevmem.survey.survey.internal.util.QuestionType
import com.nevmem.survey.survey.internal.util.typeOfQuestionForAnswer
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.transactions.transaction

internal class SimpleAnswerSaver : AnswerSaver {
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
