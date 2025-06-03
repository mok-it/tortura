package mok.it.tortura.feature.ongoingCompetition

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import mok.it.tortura.model.*
import mok.it.tortura.ui.CategoryColors

class OngoingCompetitionViewModel : ViewModel() {

    private val block = Block(
        listOf(
            Task(
                "The path of the righteous man is beset on all sides by the\n" +
                        "Inequities of the selfish and the tyranny of evil men\n" +
                        "Blessed is he who, in the name of charity and good will\n" +
                        "shepherds the weak through the valley of darkness\n" +
                        "for he is truly his brother's keeper and the finder of lost children\n" +
                        "And I will strike down upon thee with great vengeance and furious\n" +
                        "Anger those who attempt to poison and destroy my brothers\n" +
                        "And you will know\n" +
                        "My name is the Lord when I lay my vengeance upon theeThe path of the righteous man is beset on all sides by the\n" +
                        "Inequities of the selfish and the tyranny of evil men\n" +
                        "Blessed is he who, in the name of charity and good will\n" +
                        "shepherds the weak through the valley of darkness\n" +
                        "for he is truly his brother's keeper and the finder of lost children\n" +
                        "And I will strike down upon thee with great vengeance and furious\n" +
                        "Anger those who attempt to poison and destroy my brothers\n" +
                        "And you will know\n" +
                        "My name is the Lord when I lay my vengeance upon thee", "25"
            ), Task("Micimackó", "17"), Task( solution = "43" ), Task( solution = "43" ), Task( solution = "43" )
        ), 1
    )

    private val polarBlock = Block( block.tasks.map { task: Task -> Task( "JEGES ${task.text}", task.solution ) }, 2 )

    private val problemSet = ProblemSet("Teszt", listOf(block, block.copy( id = 2), block.copy( id = 3)))

    private val polarProblemSet = ProblemSet("JEGES", listOf(polarBlock, polarBlock))

    private val teams = mutableListOf(
        Team(mutableListOf(Student("Anna"), Student("Béla"))),
        Team(mutableListOf(Student("Cecil"), Student("Dénes"))),
        Team(mutableListOf(Student("Ezékiel"), Student("Ferenc"))),
        Team(mutableListOf(Student("Gábor"), Student("Hanna"))),
        Team(mutableListOf(Student("Ivett"), Student("János"))),
        Team(mutableListOf(Student("Kata"), Student("László"))),
        Team(mutableListOf(Student("Márk"), Student("Nikolett"))),
        Team(mutableListOf(Student("Olivér"), Student("Péter"))),
        Team(mutableListOf(Student("Quentin"), Student("Rebeka"))),
        Team(mutableListOf(Student("Sára"), Student("Tivadar"))),
        Team(mutableListOf(Student("Ubul"), Student("Viktor"))),
        Team(mutableListOf(Student("Wanda"), Student("Xavér"))),
        Team(mutableListOf(Student("Yvett"), Student("Zoltán"))),
        Team(mutableListOf(Student("Ádám"), Student("Borbála"))),
        Team(mutableListOf(Student("Csaba"), Student("Dorina")))
    )

    private val teamAssignment = TeamAssignment( "Kibaszott Koalák", teams, CategoryColors.BOCS )

    private val polarTeamAssignment = TeamAssignment( "Jeges", teams, CategoryColors.JEGES )

    private val _competitions = listOf(
        Competition(teamAssignment, problemSet),
        Competition(polarTeamAssignment, polarProblemSet)
    )

    val competitions = mutableStateOf(_competitions)

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
        val newCompetitionIdx = competitions.value.indexOf(competition)
        val newCompetitions = competitions.value.filter { it != competition }.toMutableList()
        newCompetitions.add(newCompetitionIdx, newValue)
        competitions.value = newCompetitions
    }
}

sealed class OnGoingCompetitionEvent {
    data class ModifyAnswer(val competition: Competition, val team: Team, val task: Task, val newAnswer: SolutionState) : OnGoingCompetitionEvent()
    data class RestartBlock(val competition: Competition, val team: Team) : OnGoingCompetitionEvent()
    data class NextBlock(val competition: Competition, val team: Team) : OnGoingCompetitionEvent()
    data class SearchTextChange( val newText: String ) : OnGoingCompetitionEvent()
    data class SearchForStudent( val searchText: String ) : OnGoingCompetitionEvent()
    data class ChangeTabIndex(val index: Int) : OnGoingCompetitionEvent()
    data class NavigateForwards( val competition: Competition, val team: Team ) : OnGoingCompetitionEvent()
    data class NavigateBackwards( val competition: Competition, val team: Team ) : OnGoingCompetitionEvent()
    data class DeleteLastTry( val competition: Competition, val team: Team ) : OnGoingCompetitionEvent()
}