package com.nevmem.survey.service.surveys.internal

import com.nevmem.survey.service.surveys.SurveysService
import com.nevmem.survey.service.surveys.data.CommonQuestionEntity
import com.nevmem.survey.service.surveys.data.QuestionEntity
import com.nevmem.survey.service.surveys.data.SurveyEntity
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.component.KoinComponent

internal class SurveysServiceImpl : SurveysService, KoinComponent {
    override suspend fun createSurvey(
        name: String,
        questions: List<QuestionEntity>,
        commonQuestion: List<CommonQuestionEntity>,
    ): SurveyEntity {
        val dto = transaction {
            val survey = SurveyEntityDTO.new {
                this.name = name
                this.active = false
            }

            questions.forEach { question ->
                QuestionEntityDTO.new {
                    when (question) {
                        is QuestionEntity.StarsQuestionEntity -> {
                            this.type = QuestionEntityType.Stars
                            this.title = question.title
                            this.stars = question.stars
                        }

                        is QuestionEntity.TextQuestionEntity -> {
                            this.type = QuestionEntityType.Text
                            this.title = question.title
                            this.maxLength = question.maxLength
                        }

                        is QuestionEntity.RatingQuestionEntity -> {
                            this.type = QuestionEntityType.Rating
                            this.title = question.title
                            this.min = question.min
                            this.max = question.max
                        }
                    }
                    this.survey = survey.id
                }
            }

            commonQuestion.forEach { commonQuestion ->
                CommonQuestionDTO.new {
                    commonQuestionId = commonQuestion.id
                    this.survey = survey.id
                }
            }

            survey
        }

        return transaction { SurveyEntityDTO.findById(dto.id)!!.toEntity() }
    }

    override suspend fun deleteSurvey(id: Long) = transaction<Unit> {
        SurveyEntityDTO.findById(id)?.delete()
    }

    override suspend fun allSurveys(): List<SurveyEntity> {
        return transaction { SurveyEntityDTO.all().map { it.toEntity() } }
    }

    override suspend fun currentActiveSurvey(): SurveyEntity? {
        return transaction {
            SurveyEntityDTO.find {
                SurveysTable.active eq true
            }.firstOrNull()?.toEntity()
        }
    }

    override suspend fun activateSurvey(id: Long) = transaction {
        SurveyEntityDTO.find {
            SurveysTable.active eq true
        }
            .forUpdate()
            .forEach {
                it.active = false
            }

        SurveyEntityDTO.findById(id)?.active = true
    }

    private fun SurveyEntityDTO.toEntity(): SurveyEntity {
        return SurveyEntity(
            id = id.value,
            name = name,
            questions = questions.map { dto ->
                when (dto.type) {
                    QuestionEntityType.Rating -> QuestionEntity.RatingQuestionEntity(
                        id = dto.id.value,
                        title = dto.title,
                        min = dto.min!!,
                        max = dto.max!!,
                    )
                    QuestionEntityType.Stars -> QuestionEntity.StarsQuestionEntity(
                        id = dto.id.value,
                        title = dto.title,
                        stars = dto.stars!!,
                    )
                    QuestionEntityType.Text -> QuestionEntity.TextQuestionEntity(
                        id = dto.id.value,
                        title = dto.title,
                        maxLength = dto.maxLength!!,
                    )
                }
            },
            commonQuestions = commonQuestions.map {
                CommonQuestionEntity(it.commonQuestionId)
            },
            active = active,
        )
    }
}
