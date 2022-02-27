package com.nevmem.survey.service.settings.internal

import com.nevmem.survey.report.report
import com.nevmem.survey.service.preferences.PreferencesService
import com.nevmem.survey.service.settings.api.Setting
import com.nevmem.survey.service.settings.api.SettingsService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private open class ReportableSetting<T>(
    private val name: String,
    private val getter: () -> T,
    private val setter: (T) -> Unit,
    private val changesFlow: Flow<Unit>,
) : Setting<T> {
    override var value: T
        get() = getter()
        set(value) {
            report(
                "setting-changed",
                mapOf("name" to name, "from" to getter(), "to" to value),
            )
            setter(value)
        }

    override val changes: Flow<T> = changesFlow.map { getter() }
}

private class ReportableBooleanSetting(
    name: String,
    preferencesService: PreferencesService,
) : ReportableSetting<Boolean>(
    name = name,
    getter = {
        preferencesService.get(name) == "true"
    },
    setter = {
        if (it) {
            preferencesService.put(name, "true")
        } else {
            preferencesService.delete(name)
        }
    },
    changesFlow = preferencesService
        .prefChanges(name)
)

/*
// Backup setting
private class BoolSettingImpl(
    private val preferencesService: PreferencesService,
    private val name: String,
) : Setting<Boolean> {
    override var value: Boolean
        get() {
            return preferencesService.get(name) == "true"
        }
        set(value) {
            if (value) {
                preferencesService.put(name, "true")
            } else {
                preferencesService.delete(name)
            }
        }

    override val changes: Flow<Boolean>
        get() = preferencesService
            .prefChanges(name)
            .map {
                value
            }
} */

internal class SettingsServiceImpl(
    private val preferencesService: PreferencesService,
) : SettingsService {
    override val isPushNotificationsEnabled: Setting<Boolean> = boolSetting("push_notifications_enabled")

    init {
        report("settings-service", "init")
    }

    private fun boolSetting(name: String) = ReportableBooleanSetting(name, preferencesService)
}
