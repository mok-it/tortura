package viewmodel

import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.runtime.mutableStateListOf
import model.Competition
import model.Student
import model.Team

class CreateTeamViewModel(val competition: Competition) {
    val teams =
        mutableStateListOf(
            Team(1, mutableListOf(Student("Ádám"), Student("Béla"), Student("Cecil")), competition),
            Team(2, mutableListOf(Student("Dénes"), Student("Elemér"), Student("Ferenc")), competition),
            Team(3, mutableListOf(Student("Géza"), Student("Hilbert"), Student("Imre")), competition)
        )

    fun moveStudent(from: LazyListItemInfo, to: LazyListItemInfo) {
        val movedStudent = getStudentById(from.key as Int)
        val fromTeam = getTeamByStudent(movedStudent)
        val toTeam = getTeamByStudent(getStudentById(to.key as Int))
        fromTeam.students.remove(movedStudent)
        toTeam.students.add(movedStudent)
    }

    fun newTeamId(): Int {
        return (teams.maxOfOrNull { it.id } ?: 0) + 1
    }

    fun getStudentById(id: Int): Student {
        for (team in teams) {
            for (student in team.students) {
                if (student.id == id) return student
            }
        }
        throw Exception("No student with the id $id")
    }

    fun getTeamByStudent(student: Student): Team {
        for (team in teams) {
            if (team.students.contains(student)) return team
        }
        throw Exception("${student.name} is in no teams")
    }
}