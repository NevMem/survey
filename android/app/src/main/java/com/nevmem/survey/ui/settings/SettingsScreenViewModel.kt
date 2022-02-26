package com.nevmem.survey.ui.settings

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.nevmem.survey.BuildConfig
import com.nevmem.survey.R
import com.nevmem.survey.service.settings.api.SettingsService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsScreenViewModel(
    background: CoroutineScope,
    settingsService: SettingsService,
) : ViewModel() {
    val uiState = mutableStateOf<List<SettingsScreenItem>>(emptyList())

    init {
        background.launch {
            withContext(Dispatchers.Main) {
                uiState.value = listOf(
                    HeaderSettingsScreenItem,
                    SwitchSettingsScreenItem(
                        R.string.notifications_enabled_setting_title,
                        settingsService.isPushNotificationsEnabled,
                    ),
                    AboutSettingsScreenItem(BuildConfig.VERSION_NAME),
                )
            }
        }
    }
}
