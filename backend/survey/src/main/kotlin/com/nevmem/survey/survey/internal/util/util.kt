package com.nevmem.survey.survey.internal.util

import com.nevmem.survey.commonQuestion.CommonQuestionEntity
import com.nevmem.survey.data.answer.QuestionAnswer
import com.nevmem.survey.question.QuestionEntity
import com.nevmem.survey.survey.UnknownCommonQuestionException

internal enum class QuestionType {
    Text,
    Rating,
    Stars,
    Radio,
}

internal fun typeOfCommonQuestion(question: CommonQuestionEntity): QuestionType {
    return when (question.id) {
        "age" -> QuestionType.Rating
        "school_name" -> QuestionType.Text
        "region" -> QuestionType.Text
        "grade" -> QuestionType.Rating
        else -> throw UnknownCommonQuestionException(question.id)
    }
}

internal fun typeOfQuestion(question: QuestionEntity): QuestionType {
    return when (question) {
        is QuestionEntity.RatingQuestionEntity -> QuestionType.Rating
        is QuestionEntity.TextQuestionEntity -> QuestionType.Text
        is QuestionEntity.StarsQuestionEntity -> QuestionType.Stars
        is QuestionEntity.RadioQuestionEntity -> QuestionType.Radio
    }
}

internal fun typeOfQuestionForAnswer(answer: QuestionAnswer): QuestionType {
    return when (answer) {
        is QuestionAnswer.TextQuestionAnswer -> QuestionType.Text
        is QuestionAnswer.StarsQuestionAnswer -> QuestionType.Stars
        is QuestionAnswer.RatingQuestionAnswer -> QuestionType.Rating
        is QuestionAnswer.RadioQuestionAnswer -> QuestionType.Radio
    }
}
