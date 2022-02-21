package com.nevmem.surveys.converters

import com.nevmem.survey.commonQuestion.CommonQuestionEntity
import com.nevmem.survey.data.question.CommonQuestion

class CommonQuestionsConverter {
    fun convertCommonQuestion(question: CommonQuestionEntity): CommonQuestion {
        return CommonQuestion(question.id)
    }

    operator fun invoke(question: CommonQuestionEntity): CommonQuestion = convertCommonQuestion(question)
}
