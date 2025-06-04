package mok.it.tortura.feature.ongoingCompetition

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import mok.it.tortura.CompetitionDb
import mok.it.tortura.model.Competition
import mok.it.tortura.model.CompetitionTeam
import mok.it.tortura.model.SolutionState
import mok.it.tortura.model.Task

class OngoingCompetitionViewModel : ViewModel() {


    val competitions = mutableStateOf(CompetitionDb.getCompetitions())

    val tabIndex = mutableStateOf(0)

    val searchText = mutableStateOf("")

    fun onEvent(event: OnGoingCompetitionEvent) {
        when (event) {
            is OnGoingCompetitionEvent.ModifyAnswer -> {
                modifyCompetition( event.competition, event.competition.answerTask( event.team, event.task, event.newAnswer ) )
            }
            is OnGoingCompetitionEvent.RestartBlock -> {
                modifyCompetition( event.competition, event.competition.restartCurrentBlock(event.team) )
            }
            is OnGoingCompetitionEvent.NextBlock -> {
                modifyCompetition( event.competition, event.competition.nextBlock(event.team) )
            }
            is OnGoingCompetitionEvent.SearchForStudent -> {
                var counter = 0
                for( competition in competitions.value ) {
                    for ( team in competition.teamAssignment.teams ){
                        for( student in team.students ){
                            if( student.name == event.searchText ){
                                tabIndex.value = counter
                                return
                            }
                        }
                    }
                    counter++
                }
            }
            is OnGoingCompetitionEvent.SearchTextChange -> {
                searchText.value = event.newText
            }

            is OnGoingCompetitionEvent.ChangeTabIndex -> {
                tabIndex.value = event.index
            }

            is OnGoingCompetitionEvent.NavigateBackwards -> {
                modifyCompetition( event.competition, event.competition.navigateBackwards( event.team ) )
            }
            is OnGoingCompetitionEvent.NavigateForwards -> {
                modifyCompetition( event.competition, event.competition.navigateForwards( event.team ) )
            }

            is OnGoingCompetitionEvent.DeleteLastTry -> {
                modifyCompetition( event.competition, event.competition.deleteLastTry( event.team ) )
            }
        }
    }

    fun getAllStudentsList() : List<String>{
        val studentList = mutableListOf<String>()
        for( competition in competitions.value ){
            for( team in competition.teamAssignment.teams ){
                for( student in team.students ){
                    studentList.add( student.name )
                }
            }
        }
        return studentList.sorted()
    }

    private fun modifyCompetition(competition: Competition, newValue: Competition) {
        CompetitionDb.saveCompetition(newValue)
        val newCompetitionIdx = competitions.value.indexOf(competition)
        val newCompetitions = competitions.value.filter { it != competition }.toMutableList()
        newCompetitions.add(newCompetitionIdx, newValue)
        competitions.value = newCompetitions
    }
}

sealed class OnGoingCompetitionEvent {
    data class ModifyAnswer(val competition: Competition, val team: CompetitionTeam, val task: Task, val newAnswer: SolutionState) : OnGoingCompetitionEvent()
    data class RestartBlock(val competition: Competition, val team: CompetitionTeam) : OnGoingCompetitionEvent()
    data class NextBlock(val competition: Competition, val team: CompetitionTeam) : OnGoingCompetitionEvent()
    data class SearchTextChange( val newText: String ) : OnGoingCompetitionEvent()
    data class SearchForStudent( val searchText: String ) : OnGoingCompetitionEvent()
    data class ChangeTabIndex(val index: Int) : OnGoingCompetitionEvent()
    data class NavigateForwards( val competition: Competition, val team: CompetitionTeam ) : OnGoingCompetitionEvent()
    data class NavigateBackwards( val competition: Competition, val team: CompetitionTeam ) : OnGoingCompetitionEvent()
    data class DeleteLastTry( val competition: Competition, val team: CompetitionTeam ) : OnGoingCompetitionEvent()
}