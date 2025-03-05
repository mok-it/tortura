package viewmodel

import androidx.lifecycle.ViewModel
import model.*

class OngoingCompetitionViewModel : ViewModel() {

    val block = Block(
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
            ), Task("Micimackó", "17")
        ), 1
    )

    val competition = ProblemSet("Teszt", listOf(block, block, block))

    val teams = mutableListOf(
        Team(mutableListOf(Student("Anna"), Student("Béla"))),
        Team(mutableListOf(Student("Cecil"), Student("Dénes"))),
        Team(mutableListOf(Student("Erik"), Student("Ferenc"))),
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


    fun onEvent(event: OnGoingCompetitionEvent) {
        when (event) {
            is OnGoingCompetitionEvent.SubmitSolution -> {

            }
        }

    }
}

sealed class OnGoingCompetitionEvent {
    data class SubmitSolution(val team: Team, val solutions: List<String>) : OnGoingCompetitionEvent()
}