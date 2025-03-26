package mok.it.tortura.feature.createTeamAssigment

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import mok.it.tortura.model.Student
import mok.it.tortura.model.Team
import mok.it.tortura.model.TeamAssignment

class CreateTeamAssignmentViewModel : ViewModel() {
    val teamAssignment =
        mutableStateOf(
            TeamAssignment(
                "JEGES",
                listOf(
                    Team(mutableListOf(Student("Ádám"), Student("Béla"), Student("Cecil"))),
                    Team(mutableListOf(Student("Dénes"), Student("Ezékiel"), Student("Ferenc"))),
                    Team(mutableListOf(Student("Géza"), Student("Hilbert"), Student("Imre")))
                )
            )
        )

    fun onEvent(event: CreateTeamAssignmentEvent) {
        when (event) {
            is CreateTeamAssignmentEvent.AddTeam -> {
                teamAssignment.value =
                    teamAssignment.value.copy(teams = teamAssignment.value.teams + Team(mutableListOf()))
            }

            is CreateTeamAssignmentEvent.AddStudent -> {
                val teamIndex = teamAssignment.value.teams.indexOf(event.team)
                val newTeam = event.team.copy(
                    students = (event.team.students + Student("", "", "")).toMutableList()
                )
                val newTeams = teamAssignment.value.teams.filter { it != event.team }.toMutableList()
                newTeams.add(teamIndex, newTeam)
                teamAssignment.value = teamAssignment.value.copy(
                    teams = newTeams
                )
            }

            is CreateTeamAssignmentEvent.DeleteTeam -> {
                teamAssignment.value =
                    teamAssignment.value.copy(teams = teamAssignment.value.teams - event.team)
            }

            is CreateTeamAssignmentEvent.DeleteMember -> {
                val newTeam =
                    event.team.copy(students = event.team.students.filter { it != event.student }.toMutableList())
                teamAssignment.value =
                    teamAssignment.value.copy(teams = teamAssignment.value.teams.filter { it != event.team } + newTeam)
            }

            is CreateTeamAssignmentEvent.ChangeStudentName -> {
                val newStudent = event.student.copy(name = event.name)
                val newTeam =
                    event.team.copy(students = (event.team.students.filter { it != event.student } + newStudent)
                        .toMutableList())
                teamAssignment.value = teamAssignment.value.copy(
                    teams = teamAssignment.value.teams.filter { it != event.team } + newTeam
                )
            }
        }
    }
}

sealed class CreateTeamAssignmentEvent {
    data object AddTeam : CreateTeamAssignmentEvent()
    data class AddStudent(val team: Team) : CreateTeamAssignmentEvent()
    data class DeleteTeam(val team: Team) : CreateTeamAssignmentEvent()
    data class DeleteMember(val team: Team, val student: Student) : CreateTeamAssignmentEvent()
    data class ChangeStudentName(val team: Team, val student: Student, val name: String) : CreateTeamAssignmentEvent()
    //TODO: change other properties of student
}