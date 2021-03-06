package com.nevmem.survey.survey.internal

import com.nevmem.survey.RandomStringGenerator
import com.nevmem.survey.commonQuestion.CommonQuestionEntity
import com.nevmem.survey.question.QuestionEntity
import com.nevmem.survey.question.QuestionVariantEntity
import com.nevmem.survey.survey.SurveyEntity
import com.nevmem.survey.survey.SurveysService
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.component.KoinComponent

private const val VARIANTS_PAIR_SEPARATOR = "@@@"
private const val VARIANTS_SEPARATOR = "=#="

private class Cache<K : Any, T : Any> {
    private val cache = mutableMapOf<K, T?>()

    fun has(key: K): Boolean {
        return cache.containsKey(key)
    }

    fun getOrNull(key: K): T? {
        return cache[key]
    }

    fun store(key: K, value: T?) {
        cache[key] = value
    }
}

private fun <K : Any, T : Any> withCache(cache: Cache<K, T>, key: K, getter: () -> T?): T? {
    if (cache.has(key)) {
        return cache.getOrNull(key)
    }
    val value = getter()
    cache.store(key, value)
    return value
}

internal class SurveysServiceImpl : SurveysService, KoinComponent {
    private val surveysCache = Cache<String, SurveyEntity>()

    override suspend fun createSurvey(
        projectId: Long,
        name: String,
        questions: List<QuestionEntity>,
        commonQuestion: List<CommonQuestionEntity>,
        answerCoolDown: Long,
    ): SurveyEntity {
        val dto = transaction {
            val survey = SurveyEntityDTO.new {
                this.name = name
                this.projectId = projectId
                this.surveyId = RandomStringGenerator.randomString(5)
                this.answerCoolDown = answerCoolDown
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

                        is QuestionEntity.RadioQuestionEntity -> {
                            this.type = QuestionEntityType.Radio
                            this.title = question.title
                            this.variants = question.variants.joinToString(VARIANTS_SEPARATOR) {
                                "${it.id}$VARIANTS_PAIR_SEPARATOR${it.variant}"
                            }
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

    override suspend fun survey(surveyId: String): SurveyEntity? = withCache(surveysCache, surveyId) {
        transaction {
            SurveyEntityDTO.find {
                SurveysTable.surveyId eq surveyId
            }.firstOrNull()?.toEntity()
        }
    }

    override suspend fun surveyById(surveyId: Long): SurveyEntity? = transaction {
        SurveyEntityDTO.find {
            SurveysTable.id eq surveyId
        }.firstOrNull()?.toEntity()
    }

    override suspend fun surveysInProject(projectId: Long): List<SurveyEntity> = transaction {
        SurveyEntityDTO.find {
            SurveysTable.projectId eq projectId
        }.map { it.toEntity() }
    }

    private fun SurveyEntityDTO.toEntity(): SurveyEntity {
        return SurveyEntity(
            id = id.value,
            projectId = projectId,
            surveyId = surveyId,
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
                    QuestionEntityType.Radio -> QuestionEntity.RadioQuestionEntity(
                        id = dto.id.value,
                        title = dto.title,
                        variants = dto.variants!!.split(VARIANTS_SEPARATOR).mapNotNull {
                            val values = it.split(VARIANTS_PAIR_SEPARATOR)
                            if (values.size == 2) {
                                QuestionVariantEntity(
                                    id = values[0],
                                    variant = values[1],
                                )
                            } else {
                                null
                            }
                        }
                    )
                }
            },
            commonQuestions = commonQuestions.map {
                CommonQuestionEntity(it.commonQuestionId)
            },
            answerCoolDown = answerCoolDown,
        )
    }
}
