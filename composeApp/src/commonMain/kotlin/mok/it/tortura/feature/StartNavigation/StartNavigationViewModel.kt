package mok.it.tortura.feature.StartNavigation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class StartNavigationViewModel : ViewModel() {
    val dialogState = mutableStateOf(StartNavigationState.NONE)

    fun onEvent(event: StartNavigationEvent) {
        when (event) {
            StartNavigationEvent.NewFromFile -> {
                dialogState.value = StartNavigationState.NEW_FROM_FILE
            }

            StartNavigationEvent.ContinueFromFile -> {
                dialogState.value = StartNavigationState.CONTINUE_FROM_FILE
            }

            StartNavigationEvent.DismissDialog -> {
                dialogState.value = StartNavigationState.NONE
            }
        }
    }

    sealed class StartNavigationEvent {
        object NewFromFile : StartNavigationEvent()
        object ContinueFromFile : StartNavigationEvent()
        object DismissDialog : StartNavigationEvent()
    }

    enum class StartNavigationState {
        NEW_FROM_FILE,
        CONTINUE_FROM_FILE,
        NONE
    }
}