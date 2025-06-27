package mok.it.tortura.feature.createTeamAssigment

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.readString
import kotlinx.coroutines.launch
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import mok.it.tortura.loadTeamAssignmentFromCsv
import mok.it.tortura.loadTeamAssignmentFromExcel
import mok.it.tortura.model.Student
import mok.it.tortura.model.Team
import mok.it.tortura.model.TeamAssignment

class CreateTeamAssignmentViewModel : ViewModel() {
    val teamAssignment = mutableStateOf(TeamAssignment( listOf()))
    val popup = mutableStateOf(CreateTeamAssignmentPopupType.NONE)

    fun onEvent(event: CreateTeamAssignmentEvent) {
        when (event) {
            is CreateTeamAssignmentEvent.AddTeam -> {
                modifyAllTeams(teamAssignment.value.teams + Team(List(4) { Student() }))
            }

            is CreateTeamAssignmentEvent.AddStudent -> {
                modifySingleTeam(event.team, event.team.copy(students = event.team.students + Student()))
            }

            is CreateTeamAssignmentEvent.DeleteTeam -> {
                modifyAllTeams(teamAssignment.value.teams - event.team)
            }

            is CreateTeamAssignmentEvent.DeleteMember -> {
                modifySingleTeam(event.team, event.team.copy(students = event.team.students - event.student))
            }

            is CreateTeamAssignmentEvent.ChangeStudentName -> {
                modifyStudent(event.team, event.student, event.student.copy(name = event.name))
            }

            is CreateTeamAssignmentEvent.ChangeStudentGroup -> {
                modifyStudent(event.team, event.student, event.student.copy(group = event.group))
            }

            is CreateTeamAssignmentEvent.ChangeStudentKlass -> {
                modifyStudent(event.team, event.student, event.student.copy(klass = event.klass))
            }

            is CreateTeamAssignmentEvent.LoadFromJson -> {
                viewModelScope.launch {
                    try {
                        val newTeamAssignment = Json.decodeFromString<TeamAssignment>(event.file.readString())
                        teamAssignment.value = newTeamAssignment
                    } catch (_: SerializationException) {
                        popup.value = CreateTeamAssignmentPopupType.PARSE_ERROR
                    } catch (_: IllegalArgumentException) {
                        popup.value = CreateTeamAssignmentPopupType.TYPE_ERROR
                    }
                }
            }

            is CreateTeamAssignmentEvent.DismissPopup -> {
                popup.value = CreateTeamAssignmentPopupType.NONE
            }

            is CreateTeamAssignmentEvent.ShowHelp -> {
                popup.value = CreateTeamAssignmentPopupType.HELP
            }

            is CreateTeamAssignmentEvent.ChangeBaseTeamId -> {
                teamAssignment.value = teamAssignment.value.copy(baseTeamId = event.baseTeamId)
            }

            is CreateTeamAssignmentEvent.LoadFromCsv -> {
                viewModelScope.launch {
                    val newTeamAssignment = loadTeamAssignmentFromCsv(event.file)
                    if( newTeamAssignment != null ) {
                        teamAssignment.value = newTeamAssignment
                    } else {
                        popup.value = CreateTeamAssignmentPopupType.CSV_ERROR
                    }
                }
            }

            is CreateTeamAssignmentEvent.LoadFromExcel -> {
                viewModelScope.launch {
                    val newTeamAssignment = loadTeamAssignmentFromExcel(event.file)
                    if (newTeamAssignment != null) {
                        teamAssignment.value = newTeamAssignment
                    } else {
                        popup.value = CreateTeamAssignmentPopupType.EXCEL_ERROR
                    }
                }
            }

        }
    }

    private fun modifyAllTeams(newValue: List<Team>) {
        teamAssignment.value = teamAssignment.value.copy(teams = newValue)
    }

    private fun modifySingleTeam(team: Team, newValue: Team) {
        val teamIndex = teamAssignment.value.teams.indexOf(team)
        if (teamIndex == -1) {
            return
        }
        val newTeams = teamAssignment.value.teams.toMutableList()
        newTeams[teamIndex] = newValue
        modifyAllTeams(newTeams)
    }

    private fun modifyStudent(team: Team, student: Student, newValue: Student) {
        val studentIndex = team.students.indexOf(student)
        if (studentIndex == -1) {
            return
        }
        val newStudents = team.students.toMutableList()
        newStudents[studentIndex] = newValue
        val newTeam = team.copy(students = newStudents)
        modifySingleTeam(team, newTeam)
    }
}

sealed class CreateTeamAssignmentEvent {
    data object AddTeam : CreateTeamAssignmentEvent()
    data class AddStudent(val team: Team) : CreateTeamAssignmentEvent()
    data class DeleteTeam(val team: Team) : CreateTeamAssignmentEvent()
    data class DeleteMember(val team: Team, val student: Student) : CreateTeamAssignmentEvent()
    data class ChangeStudentName(val team: Team, val student: Student, val name: String) : CreateTeamAssignmentEvent()
    data class ChangeStudentGroup(val team: Team, val student: Student, val group: String) : CreateTeamAssignmentEvent()
    data class ChangeStudentKlass(val team: Team, val student: Student, val klass: String) : CreateTeamAssignmentEvent()
    data class ChangeBaseTeamId(val baseTeamId: Int) : CreateTeamAssignmentEvent()
    data class LoadFromJson(val file: PlatformFile) : CreateTeamAssignmentEvent()
    data class LoadFromCsv(val file: PlatformFile) : CreateTeamAssignmentEvent()
    data class LoadFromExcel(val file: PlatformFile) : CreateTeamAssignmentEvent()
    data object DismissPopup : CreateTeamAssignmentEvent()
    data object ShowHelp : CreateTeamAssignmentEvent()
}

enum class CreateTeamAssignmentPopupType {
    PARSE_ERROR,
    TYPE_ERROR,
    CSV_ERROR,
    EXCEL_ERROR,
    HELP,
    NONE
}