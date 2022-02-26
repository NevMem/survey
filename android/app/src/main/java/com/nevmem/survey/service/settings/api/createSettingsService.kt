package com.nevmem.survey.service.settings.api

import com.nevmem.survey.service.preferences.PreferencesService
import com.nevmem.survey.service.settings.internal.SettingsServiceImpl

fun createSettingsService(
    preferencesService: PreferencesService,
): SettingsService = SettingsServiceImpl(preferencesService)
