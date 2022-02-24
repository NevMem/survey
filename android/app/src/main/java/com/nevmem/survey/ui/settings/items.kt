package com.nevmem.survey.ui.settings

sealed class SettingsScreenItem

object HeaderSettingsScreenItem : SettingsScreenItem()
data class AboutSettingsScreenItem(val version: String) : SettingsScreenItem()
