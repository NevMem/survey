package com.nevmem.survey

import android.app.Application
import com.nevmem.survey.di.createAppModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class SurveyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
//            androidLogger()
            androidContext(this@SurveyApplication)
            modules(createAppModule())
        }
    }
}
