package mok.it.tortura.feature.continueCompetition

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.vinceglb.filekit.PlatformFile
import kotlinx.coroutines.launch
import kotlinx.serialization.SerializationException
import mok.it.tortura.CompetitionDb
import mok.it.tortura.saveStringToFile
import mok.it.tortura.util.mapJsonFormat

class ContinueCompetitionViewmodel : ViewModel() {

    val rows = mutableStateOf( listOf( CompetitionsFromFile() ) )
    val popup = mutableStateOf(ContinueCompetitionPopupType.NONE)


    fun onEvent(event: ContinueCompetitionEvent) {
        when (event) {
            is ContinueCompetitionEvent.AddRow -> {
                rows.value = rows.value + CompetitionsFromFile()
            }
            is ContinueCompetitionEvent.DeleteRow -> {
                rows.value = rows.value.filter { it != event.data }
            }
            is ContinueCompetitionEvent.ChangeFile -> {

                viewModelScope.launch {
                    try {
                        val updatedRows = rows.value.toMutableList()
                        val index = updatedRows.indexOf(event.data)
                        updatedRows[index] = event.data.changeFile(event.file)
                        rows.value = updatedRows
                    } catch (_: SerializationException) {
                        popup.value = ContinueCompetitionPopupType.PARSE_ERROR
                    } catch (_: IllegalArgumentException) {
                        popup.value = ContinueCompetitionPopupType.TYPE_ERROR
                    }
                }
            }
            is ContinueCompetitionEvent.SaveCompetitionsToFile -> {
                if( null in rows.value.map { it.competitions } ){
                    TODO()
                } else {
                    viewModelScope.launch {
                        saveStringToFile(
                            event.file,
                            mapJsonFormat.encodeToString(rows.value.flatMap { it.competitions ?: emptyList() })
                        )
                    }
                }
            }
            is ContinueCompetitionEvent.SaveCompetitionsToDatabase -> {
                if( null in rows.value.map { it.competitions } ){
                    TODO()
                } else {
                    val allCompetitions = rows.value.flatMap { it.competitions ?: emptyList() }

                    CompetitionDb.overwriteDatabase( allCompetitions )
                }
            }

            is ContinueCompetitionEvent.DismissPopup -> {
                popup.value = ContinueCompetitionPopupType.NONE
            }
            ContinueCompetitionEvent.ShowHelp -> {
                popup.value = ContinueCompetitionPopupType.HELP
            }
        }
    }
}

sealed class ContinueCompetitionEvent{
    data object AddRow : ContinueCompetitionEvent()
    data class DeleteRow(val data: CompetitionsFromFile) : ContinueCompetitionEvent()
    data class ChangeFile(val data: CompetitionsFromFile, val file: PlatformFile) : ContinueCompetitionEvent()
    data class SaveCompetitionsToFile(val file: PlatformFile ) : ContinueCompetitionEvent()
    data object SaveCompetitionsToDatabase : ContinueCompetitionEvent()
    data object ShowHelp : ContinueCompetitionEvent()
    data object DismissPopup : ContinueCompetitionEvent()
}

enum class ContinueCompetitionPopupType {
    PARSE_ERROR,
    TYPE_ERROR,
    HELP,
    NONE
}