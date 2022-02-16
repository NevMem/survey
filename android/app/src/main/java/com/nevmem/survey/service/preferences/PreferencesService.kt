package com.nevmem.survey.service.preferences

import android.annotation.SuppressLint
import android.content.SharedPreferences

class PreferencesService(
    private val prefs: SharedPreferences,
) {
    @SuppressLint("ApplySharedPref")
    fun put(key: String, value: String) {
        prefs.edit()
            .putString(key, value)
            .commit()
    }

    fun get(key: String): String? = prefs.getString(key, null)
}
