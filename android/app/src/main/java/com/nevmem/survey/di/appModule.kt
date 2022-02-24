package com.nevmem.survey.di

import android.content.Context
import com.nevmem.survey.api.createNetworkService
import com.nevmem.survey.service.achievement.api.createAchievementService
import com.nevmem.survey.service.preferences.PreferencesService
import com.nevmem.survey.service.publisher.PublisherIdProvider
import com.nevmem.survey.service.survey.SurveyService
import com.nevmem.survey.ui.home.HomeScreenViewModel
import com.nevmem.survey.ui.survey.SurveyScreenViewModel
import com.nevmem.survey.ui.join.JoinScreenViewModel
import com.nevmem.survey.ui.settings.SettingsScreenViewModel
import com.nevmem.survey.ui.splash.EthnoSplashScreenViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.plus
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun createAppModule(context: Context) = module {
    single(named("background")) { CoroutineScope(Dispatchers.Default) + SupervisorJob() }
    single(named("ui")) { CoroutineScope(Dispatchers.Main) + SupervisorJob() }

    single { createNetworkService() }

    single { context.getSharedPreferences("storage", Context.MODE_PRIVATE) }
    single { PreferencesService(get()) }
    single { SurveyService(get(), get(), get()) }
    single { PublisherIdProvider() }
    single { createAchievementService(get(named("background")), get()) }

    viewModel { JoinScreenViewModel(get(), get()) }
    viewModel { EthnoSplashScreenViewModel(get(), get(named("background"))) }
    viewModel { SurveyScreenViewModel(get(), get(named("background")), get()) }
    viewModel { HomeScreenViewModel(get(named("background")), get(), get()) }
    viewModel { SettingsScreenViewModel(get(named("background"))) }
}
