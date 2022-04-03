package com.nevmem.survey.settings.api

import com.nevmem.survey.preferences.PreferencesService
import com.nevmem.survey.settings.internal.SettingsServiceImpl

fun createSettingsService(
    preferencesService: PreferencesService,
): SettingsService = SettingsServiceImpl(preferencesService)
