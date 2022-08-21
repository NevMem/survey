package com.nevmem.survey.survey.internal.saver

import com.nevmem.survey.data.answer.SurveyAnswer

internal interface AnswerSaver {
    fun save(answer: SurveyAnswer)
}
