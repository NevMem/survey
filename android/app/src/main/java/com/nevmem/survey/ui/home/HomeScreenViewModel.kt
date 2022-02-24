package com.nevmem.survey.ui.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.nevmem.survey.data.survey.Survey
import com.nevmem.survey.service.achievement.api.Achievement
import com.nevmem.survey.service.achievement.api.AchievementService
import com.nevmem.survey.service.survey.SurveyService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeScreenViewModel(
    background: CoroutineScope,
    private val achievementService: AchievementService,
    private val surveyService: SurveyService,
) : ViewModel() {
    val uiState = mutableStateOf<List<HomeScreenItem>>(emptyList())

    init {
        background.launch {
            combine(
                surveyService.surveys,
                achievementService.achievements,
            ) { survey: Survey?, achievements: List<Achievement> ->
                listOf(HomeScreenHeader, achievements.item(), survey.item())
            }.collect {
                withContext(Dispatchers.Main) {
                    uiState.value = it
                }
            }
        }
    }

    fun leaveSurvey() {
        surveyService.leaveSurvey()
    }

    private fun Survey?.item(): HomeScreenItem {
        if (this == null) {
            return SurveyState.NoSurvey
        }
        return SurveyState.ReadySurvey(this)
    }

    private fun List<Achievement>.item(): HomeScreenItem {
        return AchievementsState(this)
    }
}
