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
                    teamAssignment.value.copy(
                        teams = teamAssignment.value.teams + Team(mutableListOf( Student(), Student(), Student(), Student() ))
                    )
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
                modifyTeams( teamAssignment.value.teams - event.team )
            }

            is CreateTeamAssignmentEvent.DeleteMember -> {
                modifyTeam( event.team, event.team.copy( students = event.team.students - event.student ) )
            }

            is CreateTeamAssignmentEvent.ChangeStudentName -> {
                modifyStudent( event.team, event.student, event.student.copy( name = event.student.name ) )
            }

            is CreateTeamAssignmentEvent.ChangeStudentGroup -> {
                modifyStudent( event.team, event.student, event.student.copy( group = event.group ) )
            }

            is CreateTeamAssignmentEvent.ChangeStudentKlass -> {
                modifyStudent( event.team, event.student, event.student.copy( klass = event.klass ) )
            }
        }
    }

    private fun modifyTeams( newTeams: List<Team> ) {
        teamAssignment.value = teamAssignment.value.copy( teams = newTeams )
    }

    private fun modifyTeam( team: Team, newValue: Team ) {
        val teamIndex = teamAssignment.value.teams.indexOf(team)
        val newTeams = teamAssignment.value.teams.filter { it != team }.toMutableList()
        newTeams.add(teamIndex, newValue)
        modifyTeams( newTeams )
    }

    private fun modifyStudent( team: Team, student: Student, newValue: Student ){
        val studentIndex = team.students.indexOf(student)
        val newStudents = team.students.filter { it != student }.toMutableList()
        newStudents.add(studentIndex, newValue)
        val newTeam = team.copy( students = newStudents )
        modifyTeam(team, newTeam)
    }
}

sealed class CreateTeamAssignmentEvent {
    data object AddTeam : CreateTeamAssignmentEvent()
    data class AddStudent(val team: Team) : CreateTeamAssignmentEvent()
    data class DeleteTeam(val team: Team) : CreateTeamAssignmentEvent()
    data class DeleteMember(val team: Team, val student: Student) : CreateTeamAssignmentEvent()
    data class ChangeStudentName(val team: Team, val student: Student, val name: String) : CreateTeamAssignmentEvent()
    data class ChangeStudentGroup( val team: Team, val student: Student, val group: String ) : CreateTeamAssignmentEvent()
    data class ChangeStudentKlass( val team: Team, val student: Student, val klass: String ) : CreateTeamAssignmentEvent()
}