package viewmodel

import androidx.lifecycle.ViewModel
import model.*

class OngoingCompetitionViewModel: ViewModel() {

    val block = Block( listOf(
        Task("The path of the righteous man is beset on all sides by the\n" +
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
                "My name is the Lord when I lay my vengeance upon thee", "25"), Task("Micimackó","17")
    ),1 )

    val competition = Competition( "Teszt", listOf(block, block, block) )

    val teams = mutableListOf(
        Team(101, mutableListOf(Student("Anna"), Student("Béla")), competition),
        Team(102, mutableListOf(Student("Cecil"), Student("Dénes")), competition),
        Team(103, mutableListOf(Student("Erik"), Student("Ferenc")), competition),
        Team(104, mutableListOf(Student("Gábor"), Student("Hanna")), competition),
        Team(105, mutableListOf(Student("Ivett"), Student("János")), competition),
        Team(106, mutableListOf(Student("Kata"), Student("László")), competition),
        Team(107, mutableListOf(Student("Márk"), Student("Nikolett")), competition),
        Team(108, mutableListOf(Student("Olivér"), Student("Péter")), competition),
        Team(109, mutableListOf(Student("Quentin"), Student("Rebeka")), competition),
        Team(110, mutableListOf(Student("Sára"), Student("Tivadar")), competition),
        Team(111, mutableListOf(Student("Ubul"), Student("Viktor")), competition),
        Team(112, mutableListOf(Student("Wanda"), Student("Xavér")), competition),
        Team(113, mutableListOf(Student("Yvett"), Student("Zoltán")), competition),
        Team(114, mutableListOf(Student("Ádám"), Student("Borbála")), competition),
        Team(115, mutableListOf(Student("Csaba"), Student("Dorina")), competition)
    )


    fun onEvent(event: OnGoingCompetitionEvent){
        when(event){
            is OnGoingCompetitionEvent.SubmitSolution->{

            }
        }

    }
}

sealed class OnGoingCompetitionEvent{
    data class SubmitSolution( val team: Team, val solutions: List<String> ) : OnGoingCompetitionEvent()
}