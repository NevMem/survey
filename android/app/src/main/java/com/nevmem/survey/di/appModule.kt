package com.nevmem.survey.di

import android.content.Context
import com.nevmem.survey.network.api.BackendBaseUrlProvider
import com.nevmem.survey.network.api.createNetworkService
import com.nevmem.survey.service.achievement.api.createAchievementService
import com.nevmem.survey.service.network.BackendBaseUrlProviderImpl
import com.nevmem.survey.service.preferences.PreferencesService
import com.nevmem.survey.service.push.api.createPushService
import com.nevmem.survey.service.settings.api.createSettingsService
import com.nevmem.survey.service.survey.SurveyService
import com.nevmem.survey.service.uid.UserIdProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.plus
import org.koin.core.qualifier.named
import org.koin.dsl.module

private const val BACKGROUND_SCOPE = "background"
internal val BACKGROUND_SCOPE_QUALIFIER = named(BACKGROUND_SCOPE)

private const val UI_SCOPE = "ui"
internal val UI_SCOPE_QUALIFIER = named(UI_SCOPE)

fun createAppModule(context: Context) = module {
    single(BACKGROUND_SCOPE_QUALIFIER) { CoroutineScope(Dispatchers.Default) + SupervisorJob() }
    single(UI_SCOPE_QUALIFIER) { CoroutineScope(Dispatchers.Main) + SupervisorJob() }

    single<BackendBaseUrlProvider> { BackendBaseUrlProviderImpl(get()) }

    single { createNetworkService(get()) }

    single { context.getSharedPreferences("storage", Context.MODE_PRIVATE) }
    single { PreferencesService(get(BACKGROUND_SCOPE_QUALIFIER), get()) }
    single { SurveyService(get(), get(), get()) }
    single { UserIdProvider() }
    single { createAchievementService(get(BACKGROUND_SCOPE_QUALIFIER), get()) }
    single { createSettingsService(get()) }
    single { createPushService(get(BACKGROUND_SCOPE_QUALIFIER), get(), get(), get()) }
}
