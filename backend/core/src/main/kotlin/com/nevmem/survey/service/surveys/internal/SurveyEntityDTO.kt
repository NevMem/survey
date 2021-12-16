package com.nevmem.survey.service.surveys.internal

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable

internal object SurveysTable : LongIdTable() {
    val name = text("name")
    val active = bool("active")
}

internal enum class QuestionEntityType {
    Stars,
    Text,
    Rating,
}

internal object QuestionsTable : LongIdTable() {
    val title = varchar("title", 256)
    val type = enumeration("type", QuestionEntityType::class)
    val min = integer("min").nullable()
    val max = integer("max").nullable()
    val stars = integer("stars").nullable()
    val maxLength = integer("maxLength").nullable()

    val survey = reference("survey", SurveysTable)
}

internal class SurveyEntityDTO(id: EntityID<Long>): LongEntity(id) {
    companion object : LongEntityClass<SurveyEntityDTO>(SurveysTable)

    var name by SurveysTable.name
    var active by SurveysTable.active
    val questions by QuestionEntityDTO referrersOn QuestionsTable.survey
}

internal class QuestionEntityDTO(id: EntityID<Long>): LongEntity(id) {
    companion object : LongEntityClass<QuestionEntityDTO>(QuestionsTable)

    var title by QuestionsTable.title
    var type by QuestionsTable.type
    var min by QuestionsTable.min
    var max by QuestionsTable.max
    var stars by QuestionsTable.stars
    var maxLength by QuestionsTable.maxLength

    var survey by QuestionsTable.survey
}
