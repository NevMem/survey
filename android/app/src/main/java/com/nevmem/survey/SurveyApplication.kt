package com.nevmem.survey

import android.app.Application
import com.nevmem.survey.di.createAppModule
import com.nevmem.survey.di.createViewModelsModule
import com.yandex.metrica.YandexMetrica
import com.yandex.metrica.YandexMetricaConfig
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class SurveyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        initMetrica()

        startKoin {
            androidContext(this@SurveyApplication)
            modules(
                createAppModule(this@SurveyApplication),
                createViewModelsModule(),
            )
        }
    }

    private fun initMetrica() {
        val config = YandexMetricaConfig
            .newConfigBuilder("96beca93-fe1f-4ea0-a244-c999dac71a4d")
            .build()
        YandexMetrica.activate(applicationContext, config)
        YandexMetrica.enableActivityAutoTracking(this)
    }
}
