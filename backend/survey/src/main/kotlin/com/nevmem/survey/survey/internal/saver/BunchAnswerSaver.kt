package com.nevmem.survey.survey.internal.saver

import com.nevmem.survey.data.answer.QuestionAnswer
import com.nevmem.survey.data.answer.SurveyAnswer
import com.nevmem.survey.survey.AnswersServiceMetrics
import com.nevmem.survey.survey.internal.QuestionAnswerDTO
import com.nevmem.survey.survey.internal.SurveyAnswerDTO
import com.nevmem.survey.survey.internal.SurveyAnswerType
import com.nevmem.survey.survey.internal.util.QuestionType
import com.nevmem.survey.survey.internal.util.typeOfQuestionForAnswer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.transactions.transaction

internal class BunchAnswerSaver(
    backgroundScope: CoroutineScope,
) : AnswerSaver {
    private val queue = mutableListOf<SurveyAnswer>()

    init {
        backgroundScope.launch {
            withContext(Dispatchers.Default) {
                while (true) {
                    val answersToSave = (0 until 2048).mapNotNull { queue.removeFirstOrNull() }

                    try {
                        withContext(Dispatchers.IO) {
                            transaction {
                                answersToSave.forEach { answer ->
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
                                            this.jsonAnswer = Json.encodeToString(
                                                QuestionAnswer.serializer(),
                                                actualAnswer,
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        updateMetrics()
                    } catch (exception: Exception) {
                        println(exception)
                        queue.addAll(answersToSave)
                        updateMetrics()
                    }
                    if (queue.isEmpty()) {
                        delay(500L)
                    }
                }
            }
        }
    }

    override fun save(answer: SurveyAnswer) {
        queue.add(answer)
        updateMetrics()
    }

    private fun updateMetrics() {
        AnswersServiceMetrics.answersQueueSizeObserver.updateValue(queue.size.toDouble())
    }
}
