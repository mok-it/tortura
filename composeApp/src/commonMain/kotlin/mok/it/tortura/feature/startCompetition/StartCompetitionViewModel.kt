package mok.it.tortura.feature.startCompetition

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.vinceglb.filekit.PlatformFile
import kotlinx.coroutines.launch
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import mok.it.tortura.CompetitionDb
import mok.it.tortura.model.Competition
import mok.it.tortura.saveStringToFile

class StartCompetitionViewModel : ViewModel() {
    val rows = mutableStateOf(listOf(CompetitionDataFromFile()))
    val popup = mutableStateOf(StartCompetitionPopupType.NONE)
    val canSave
        get() = rows.value.none {
            it.problemSet == null
                    || it.problemSetFile == null
                    || it.teamAssignment == null
                    || it.teamAssignmentFile == null
        }

    fun onEvent(event: StartCompetitionEvent) {
        when (event) {
            is StartCompetitionEvent.AddRow -> {
                rows.value = rows.value + CompetitionDataFromFile()
            }

            is StartCompetitionEvent.RemoveRow -> {
                rows.value = rows.value.filter { it != event.data }
            }

            is StartCompetitionEvent.SelectTeamAssignment -> {
                viewModelScope.launch {
                    try {
                        val newData = event.data.changeTeamAssignmentFile(event.file)
                        val index = rows.value.indexOf(event.data)
                        val newRow = rows.value.filter { it != event.data }.toMutableList()
                        newRow.add(index, newData)
                        rows.value = newRow
                    } catch (_: SerializationException) {
                        popup.value = StartCompetitionPopupType.PARSE_ERROR
                    } catch (_: IllegalArgumentException) {
                        popup.value = StartCompetitionPopupType.TYPE_ERROR
                    }
                }
            }

            is StartCompetitionEvent.SelectProblemSet -> {
                viewModelScope.launch {
                    try {
                        val newData = event.data.changeProblemSetFile(event.file)
                        val index = rows.value.indexOf(event.data)
                        val newRow = rows.value.filter { it != event.data }.toMutableList()
                        newRow.add(index, newData)
                        rows.value = newRow
                    } catch (_: SerializationException) {
                        popup.value = StartCompetitionPopupType.PARSE_ERROR
                    } catch (_: IllegalArgumentException) {
                        popup.value = StartCompetitionPopupType.TYPE_ERROR
                    }
                }
            }

            is StartCompetitionEvent.SaveToFile -> {
                if (null in rows.value.map { it.problemSet } || null in rows.value.map { it.teamAssignment }) {
                    popup.value = StartCompetitionPopupType.SAVE_ERROR
                } else {
                    viewModelScope.launch {
                        saveStringToFile(
                            event.file, Json.encodeToString(
                                rows.value.map {
                                    Competition(it.teamAssignment!!, it.problemSet!!)
                                }
                            ))
                    }
                }
            }

            is StartCompetitionEvent.SaveToDatabase -> {
                if (null in rows.value.map { it.problemSet } || null in rows.value.map { it.teamAssignment }) {
                    popup.value = StartCompetitionPopupType.SAVE_ERROR
                } else {
                    CompetitionDb.overwriteDatabase(
                        rows.value.map {
                            Competition(it.teamAssignment!!, it.problemSet!!)
                        }
                    )
                }
            }
        }
    }

    sealed class StartCompetitionEvent {
        data object AddRow : StartCompetitionEvent()
        data class RemoveRow(val data: CompetitionDataFromFile) : StartCompetitionEvent()
        data class SelectTeamAssignment(val data: CompetitionDataFromFile, val file: PlatformFile) :
            StartCompetitionEvent()

        data class SelectProblemSet(val data: CompetitionDataFromFile, val file: PlatformFile) : StartCompetitionEvent()
        data class SaveToFile(val file: PlatformFile) : StartCompetitionEvent()
        data object SaveToDatabase : StartCompetitionEvent()
    }

    enum class StartCompetitionPopupType {
        PARSE_ERROR,
        TYPE_ERROR,
        SAVE_ERROR,
        HELP,
        NONE
    }
}