package com.nevmem.survey.settings.api

interface SettingsService {
    val isPushNotificationsEnabled: Setting<Boolean>
    val isHttpBackendUrlEnabled: Setting<Boolean>
    val enableNotUniqueUserIds: Setting<Boolean>
}
