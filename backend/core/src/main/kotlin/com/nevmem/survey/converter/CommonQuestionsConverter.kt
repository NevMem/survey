package com.nevmem.survey.converter

import com.nevmem.survey.data.question.CommonQuestion
import com.nevmem.survey.service.surveys.data.CommonQuestionEntity

class CommonQuestionsConverter {
    fun convertCommonQuestion(question: CommonQuestionEntity): CommonQuestion {
        return CommonQuestion(question.id)
    }

    operator fun invoke(question: CommonQuestionEntity): CommonQuestion = convertCommonQuestion(question)
}
