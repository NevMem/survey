package com.nevmem.survey.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.plus
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun createAppModule() = module {
    single(named("background")) { CoroutineScope(Dispatchers.Default) + SupervisorJob() }
    single(named("ui")) { CoroutineScope(Dispatchers.Main) + SupervisorJob() }
}
