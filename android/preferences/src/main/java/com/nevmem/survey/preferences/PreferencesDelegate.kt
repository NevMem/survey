package com.nevmem.survey.preferences

interface PreferencesDelegate {
    fun put(key: String, value: String)
    fun get(key: String): String?
    fun delete(key: String)
}
