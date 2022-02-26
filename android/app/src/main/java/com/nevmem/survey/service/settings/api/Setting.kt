package com.nevmem.survey.service.settings.api

import kotlinx.coroutines.flow.Flow

interface Setting<T> {
    var value: T
    val changes: Flow<T>
}
