package com.nevmem.survey.di

import android.content.Context
import com.nevmem.survey.network.api.createNetworkService
import com.nevmem.survey.service.achievement.api.createAchievementService
import com.nevmem.survey.service.preferences.PreferencesService
import com.nevmem.survey.service.push.api.createPushService
import com.nevmem.survey.service.settings.api.SettingsService
import com.nevmem.survey.service.settings.api.createSettingsService
import com.nevmem.survey.service.survey.SurveyService
import com.nevmem.survey.service.uid.UserIdProvider
import com.nevmem.survey.ui.home.HomeScreenViewModel
import com.nevmem.survey.ui.join.JoinScreenViewModel
import com.nevmem.survey.ui.settings.DevSettingsScreenViewModel
import com.nevmem.survey.ui.settings.SettingsScreenViewModel
import com.nevmem.survey.ui.splash.EthnoSplashScreenViewModel
import com.nevmem.survey.ui.survey.SurveyScreenViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.plus
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

private const val BACKGROUND_SCOPE = "background"
internal val BACKGROUND_SCOPE_QUALIFIER = named(BACKGROUND_SCOPE)

private const val UI_SCOPE = "ui"
internal val UI_SCOPE_QUALIFIER = named(UI_SCOPE)

fun createAppModule(context: Context) = module {
    single(BACKGROUND_SCOPE_QUALIFIER) { CoroutineScope(Dispatchers.Default) + SupervisorJob() }
    single(UI_SCOPE_QUALIFIER) { CoroutineScope(Dispatchers.Main) + SupervisorJob() }

    single(named("backend_base_url")) {
        val settingService: SettingsService = get()
        if (settingService.isHttpBackendUrlEnabled.value) {
            "http://ethnosurvey.com"
        } else {
            "https://ethnosurvey.com"
        }
    }

    single { createNetworkService(get(named("backend_base_url"))) }

    single { context.getSharedPreferences("storage", Context.MODE_PRIVATE) }
    single { PreferencesService(get(BACKGROUND_SCOPE_QUALIFIER), get()) }
    single { SurveyService(get(), get(), get()) }
    single { UserIdProvider() }
    single { createAchievementService(get(BACKGROUND_SCOPE_QUALIFIER), get()) }
    single { createSettingsService(get()) }
    single { createPushService(get(BACKGROUND_SCOPE_QUALIFIER), get(), get(), get()) }
}
