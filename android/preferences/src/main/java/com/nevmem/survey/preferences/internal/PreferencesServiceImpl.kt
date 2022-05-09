package com.nevmem.survey.preferences.internal

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.nevmem.survey.preferences.PreferencesDelegate
import com.nevmem.survey.preferences.PreferencesService
import com.nevmem.survey.report.report
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

internal class PreferencesServiceImpl(
    private val background: CoroutineScope,
    private val prefs: SharedPreferences,
) : PreferencesService {
    private val changes = MutableSharedFlow<String>(1)

    private val prefsListener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        report("preference-changed", mapOf("key" to key))
        background.launch {
            changes.emit(key)
        }
    }

    init {
        report("preferences-service", "init")
        prefs.registerOnSharedPreferenceChangeListener(prefsListener)
    }

    @SuppressLint("ApplySharedPref")
    override fun put(key: String, value: String) {
        prefs.edit()
            .putString(key, value)
            .commit()
    }

    override fun get(key: String): String? = prefs.getString(key, null)

    override fun delete(key: String) = prefs.edit().remove(key).apply()

    override fun prefChanges(key: String): Flow<Unit> = changes.filter { it == key }.map { }

    override fun createDelegate(namespace: String): PreferencesDelegate {
        val service = this
        return object : PreferencesDelegate {
            override fun put(key: String, value: String) = service.put("$namespace.$key", value)
            override fun get(key: String): String? = service.get("$namespace.$key")
            override fun delete(key: String) = service.delete("$namespace.$key")
        }
    }
}
