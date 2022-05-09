package com.nevmem.survey.preferences

import android.content.SharedPreferences
import com.nevmem.survey.preferences.internal.PreferencesServiceImpl
import kotlinx.coroutines.CoroutineScope

fun createPreferencesService(
    background: CoroutineScope,
    prefs: SharedPreferences,
): PreferencesService = PreferencesServiceImpl(background, prefs)
