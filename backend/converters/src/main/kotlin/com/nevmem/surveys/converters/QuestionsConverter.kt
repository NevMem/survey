package com.nevmem.surveys.converters

import com.nevmem.survey.data.question.Question
import com.nevmem.survey.data.question.QuestionVariant
import com.nevmem.survey.question.QuestionEntity

class QuestionsConverter {
    private fun convertQuestion(question: QuestionEntity): Question {
        return when (question) {
            is QuestionEntity.RatingQuestionEntity -> Question.RatingQuestion(
                title = question.title,
                min = question.min,
                max = question.max,
            )
            is QuestionEntity.StarsQuestionEntity -> Question.StarsQuestion(
                title = question.title,
                stars = question.stars,
            )
            is QuestionEntity.TextQuestionEntity -> Question.TextQuestion(
                title = question.title,
                maxLength = question.maxLength,
            )
            is QuestionEntity.RadioQuestionEntity -> Question.RadioQuestion(
                title = question.title,
                variants = question.variants.map { QuestionVariant(it.id, it.variant) },
            )
        }
    }

    operator fun invoke(question: QuestionEntity): Question = convertQuestion(question)
}
