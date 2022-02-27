package com.nevmem.survey.ui.settings

import androidx.annotation.StringRes
import com.nevmem.survey.service.settings.api.Setting

sealed class SettingsScreenItem

object HeaderSettingsScreenItem : SettingsScreenItem()
data class AboutSettingsScreenItem(val version: String) : SettingsScreenItem()

object DeveloperSettingsHomeScreenItem : SettingsScreenItem()

data class SwitchSettingsScreenItem(
    @StringRes val title: Int,
    val setting: Setting<Boolean>,
) : SettingsScreenItem()
