package com.nevmem.survey.converter

import com.nevmem.survey.data.question.Question
import com.nevmem.survey.service.surveys.data.QuestionEntity

class QuestionsConverter {
    fun convertQuestion(question: QuestionEntity): Question {
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
        }
    }

    operator fun invoke(question: QuestionEntity): Question = convertQuestion(question)
}