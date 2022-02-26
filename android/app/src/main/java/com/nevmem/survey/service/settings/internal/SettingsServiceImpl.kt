package com.nevmem.survey.service.settings.internal

import com.nevmem.survey.service.preferences.PreferencesService
import com.nevmem.survey.service.settings.api.Setting
import com.nevmem.survey.service.settings.api.SettingsService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

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

    override val changes: Flow<Boolean> = preferencesService
        .prefChanges(name)
        .map {
            value
        }
}

internal class SettingsServiceImpl(
    private val preferencesService: PreferencesService,
) : SettingsService {
    override val isPushNotificationsEnabled: Setting<Boolean> = boolSetting("push_notifications_enabled")

    private fun boolSetting(name: String) = BoolSettingImpl(preferencesService, name)

    /* private fun boolSetting(name: String): Setting<Boolean> {
        return object : Setting<Boolean> {
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

            override val changes: Flow<Boolean> = preferencesService.prefChanges(name).map {
                value
            }
        }
    } */
}
