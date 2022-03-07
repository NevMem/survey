package com.nevmem.survey.survey.internal

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ReferenceOption

internal object SurveysTable : LongIdTable() {
    val name = text("name")
    val surveyId = varchar("surveyId", 8)
    val answerCoolDown = long("answerCoolDown")
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

    val survey = reference("survey", SurveysTable, onDelete = ReferenceOption.CASCADE)
}

internal object CommonQuestionsTable : LongIdTable() {
    val commonQuestionId = varchar("commonQuestionId", 16)

    val survey = reference("survey", SurveysTable, onDelete = ReferenceOption.CASCADE)
}

internal class SurveyEntityDTO(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<SurveyEntityDTO>(SurveysTable)

    var name by SurveysTable.name
    var surveyId by SurveysTable.surveyId
    var answerCoolDown by SurveysTable.answerCoolDown
    val questions by QuestionEntityDTO referrersOn QuestionsTable.survey
    val commonQuestions by CommonQuestionDTO referrersOn CommonQuestionsTable.survey
}

internal class QuestionEntityDTO(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<QuestionEntityDTO>(QuestionsTable)

    var title by QuestionsTable.title
    var type by QuestionsTable.type
    var min by QuestionsTable.min
    var max by QuestionsTable.max
    var stars by QuestionsTable.stars
    var maxLength by QuestionsTable.maxLength

    var survey by QuestionsTable.survey
}

internal class CommonQuestionDTO(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<CommonQuestionDTO>(CommonQuestionsTable)

    var commonQuestionId by CommonQuestionsTable.commonQuestionId

    var survey by CommonQuestionsTable.survey
}
