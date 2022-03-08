package com.nevmem.survey.service.settings.api

interface SettingsService {
    val isPushNotificationsEnabled: Setting<Boolean>
    val isHttpBackendUrlEnabled: Setting<Boolean>
}
