package mok.it.tortura.feature.StartNavigation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.readString
import kotlinx.coroutines.launch
import mok.it.tortura.CompetitionDb
import mok.it.tortura.model.Competition
import mok.it.tortura.util.mapJsonFormat

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

            is StartNavigationEvent.LoadFromFile -> {
                viewModelScope.launch {
                    val competitions = mapJsonFormat.decodeFromString<List<Competition>>(
                        event.file.readString()
                    )
                    CompetitionDb.overwriteDatabase(competitions)
                    event.navigation.invoke()
                }
            }
        }
    }

    sealed class StartNavigationEvent {
        object NewFromFile : StartNavigationEvent()
        object ContinueFromFile : StartNavigationEvent()
        object DismissDialog : StartNavigationEvent()
        class LoadFromFile(val file: PlatformFile, val navigation: () -> Unit) : StartNavigationEvent()
    }

    enum class StartNavigationState {
        NEW_FROM_FILE,
        CONTINUE_FROM_FILE,
        NONE
    }
}