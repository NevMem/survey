package com.nevmem.survey.ui.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.nevmem.survey.data.survey.Survey
import com.nevmem.survey.service.achievement.api.Achievement
import com.nevmem.survey.service.achievement.api.AchievementService
import com.nevmem.survey.service.preferences.PreferencesService
import com.nevmem.survey.service.survey.SurveyService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeScreenViewModel(
    background: CoroutineScope,
    private val achievementService: AchievementService,
    private val surveyService: SurveyService,
    private val preferencesService: PreferencesService,
) : ViewModel() {
    val uiState = mutableStateOf<List<HomeScreenItem>>(emptyList())

    private var job: Job? = null

    init {
        job = background.launch {
            combine(
                surveyService.surveys,
                achievementService.achievements,
            ) { survey: Survey?, achievements: List<Achievement> ->
                listOf(
                    HomeScreenHeader,
                    achievements.item(),
                    survey.item(),
                    HomeScreenFooter(survey != null),
                )
            }.collect {
                withContext(Dispatchers.Main) {
                    uiState.value = it
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

    fun leaveSurvey() {
        surveyService.leaveSurvey()
    }

    private fun Survey?.item(): HomeScreenItem {
        if (this == null) {
            return SurveyState.NoSurvey
        }
        val answeredCurrentSurveyAt =
            preferencesService.get("answered-survey-${id}")?.toLongOrNull() ?: 0
        if (answeredCurrentSurveyAt + surveyCoolDown >= System.currentTimeMillis() || 2 == 2) {
            return SurveyState.AlreadyAnsweredSurvey(this)
        }
        return SurveyState.ReadySurvey(this)
    }

    private fun List<Achievement>.item(): HomeScreenItem {
        return AchievementsState(this)
    }
}
