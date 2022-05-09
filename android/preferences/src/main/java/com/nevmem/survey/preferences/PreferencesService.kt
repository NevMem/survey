package com.nevmem.survey.preferences

import kotlinx.coroutines.flow.Flow

interface PreferencesService {
    fun put(key: String, value: String)
    fun get(key: String): String?
    fun delete(key: String)
    fun prefChanges(key: String): Flow<Unit>

    fun createDelegate(namespace: String): PreferencesDelegate
}
