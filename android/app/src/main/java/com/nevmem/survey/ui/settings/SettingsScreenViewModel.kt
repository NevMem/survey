package com.nevmem.survey.ui.settings

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.nevmem.survey.BuildConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsScreenViewModel(
    background: CoroutineScope,
) : ViewModel() {
    val uiState = mutableStateOf<List<SettingsScreenItem>>(emptyList())

    init {
        background.launch {
            withContext(Dispatchers.Main) {
                uiState.value = listOf(
                    HeaderSettingsScreenItem,
                    AboutSettingsScreenItem(BuildConfig.VERSION_NAME),
                )
            }
        }
    }
}
