package com.nevmem.survey.service.preferences

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.nevmem.survey.report.report
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class PreferencesService(
    private val background: CoroutineScope,
    private val prefs: SharedPreferences,
) {
    private val changes = MutableSharedFlow<String>(1)

    init {
        report("preferences-service", "init")
        prefs.registerOnSharedPreferenceChangeListener { _, key ->
            background.launch {
                changes.emit(key)
            }
        }
    }

    @SuppressLint("ApplySharedPref")
    fun put(key: String, value: String) {
        prefs.edit()
            .putString(key, value)
            .commit()
    }

    fun get(key: String): String? = prefs.getString(key, null)

    fun delete(key: String) = prefs.edit().remove(key).apply()

    fun prefChanges(key: String): Flow<Unit> = changes.filter { it == key }.map { }
}
