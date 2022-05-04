package com.nevmem.surveys.converters

import com.nevmem.survey.data.survey.Survey
import com.nevmem.survey.survey.SurveyEntity
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SurveysConverter : KoinComponent {
    private val questionsConverter by inject<QuestionsConverter>()
    private val commonQuestionsConverter by inject<CommonQuestionsConverter>()

    fun convertSurvey(survey: SurveyEntity): Survey {
        return Survey(
            id = survey.id,
            projectId = survey.projectId,
            surveyId = survey.surveyId,
            name = survey.name,
            questions = survey.questions.map { questionsConverter(it) },
            commonQuestions = survey.commonQuestions.map { commonQuestionsConverter(it) },
            surveyCoolDown = survey.answerCoolDown,
        )
    }

    operator fun invoke(survey: SurveyEntity): Survey = convertSurvey(survey)
}
