package com.nevmem.survey.survey.internal

import com.nevmem.survey.TableNames
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable

enum class SurveyAnswerType {
    Stars,
    Text,
    Rating,
    Radio,
}

internal object SurveyAnswerTable : LongIdTable(TableNames.surveyAnswerTableName) {
    val surveyId = varchar("surveyId", 64)
    val publisherId = varchar("publisherId", 256)
    val mediaGalleryId = long("mediaGalleryId").nullable()
    val timestamp = long("timestamp")
}

internal object QuestionAnswerTable : LongIdTable(TableNames.questionAnswerTableName) {
    val jsonAnswer = text("jsonAnswer", eagerLoading = true)
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
    var mediaGalleryId by SurveyAnswerTable.mediaGalleryId
    var timestamp by SurveyAnswerTable.timestamp
}
