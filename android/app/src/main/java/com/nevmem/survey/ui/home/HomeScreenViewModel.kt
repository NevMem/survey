package com.nevmem.survey.ui.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.qualifier.named
import org.koin.java.KoinJavaComponent.inject

class HomeScreenViewModel : ViewModel() {

    private val background by injectBackgroundScope()

    sealed class UiState {
        object None : UiState()
        object Loading : UiState()
        object Success : UiState()
        data class Error(val message: String) : UiState()
    }

    internal val state = mutableStateOf<UiState>(UiState.None)

    private var job: Job? = null

    internal fun tryFetchSurvey(surveyId: String) {
        job?.cancel()
        job = background.launch {
            withContext(Dispatchers.Main) {
                state.value = UiState.Loading
            }
            delay(150L)
            withContext(Dispatchers.Main) {
                state.value = UiState.Error("Some error while loading survey $surveyId")
            }
        }
    }
}

fun ViewModel.injectBackgroundScope() = inject<CoroutineScope>(CoroutineScope::class.java, qualifier = named("background"))
fun ViewModel.injectUiScope() = inject<CoroutineScope>(CoroutineScope::class.java, qualifier = named("ui"))
