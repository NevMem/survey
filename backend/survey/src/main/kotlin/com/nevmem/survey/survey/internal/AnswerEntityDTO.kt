package com.nevmem.survey.survey.internal

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable

enum class SurveyAnswerType {
    Stars,
    Text,
    Rating,
}

internal object SurveyAnswerTable : LongIdTable() {
    val surveyId = varchar("surveyId", 64)
    val publisherId = varchar("publisherId", 256)
}

internal object QuestionAnswerTable : LongIdTable() {
    val jsonAnswer = text("jsonAnswer")
    val type = enumeration("type", SurveyAnswerType::class)

    val surveyAnswer = reference("surveyAnswer", SurveyAnswerTable)
}

internal class QuestionAnswerDTO(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<QuestionAnswerDTO>(QuestionAnswerTable)

    var jsonAnswer by QuestionAnswerTable.jsonAnswer
    var type by QuestionAnswerTable.type

    var surveyAnswer by QuestionAnswerTable.surveyAnswer
}

internal class SurveyAnswerDTO(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<SurveyAnswerDTO>(SurveyAnswerTable)

    var publisherId by SurveyAnswerTable.publisherId
    var surveyId by SurveyAnswerTable.surveyId
    val answers by QuestionAnswerDTO referrersOn QuestionAnswerTable.surveyAnswer
}
