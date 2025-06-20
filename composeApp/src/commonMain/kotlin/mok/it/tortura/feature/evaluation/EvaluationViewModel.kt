package mok.it.tortura.feature.evaluation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import mok.it.tortura.CompetitionDb

class EvaluationViewModel : ViewModel() {
    val competitions = mutableStateOf(CompetitionDb.getCompetitions())
    val tabIndex = mutableStateOf(0)

    val x = competitions.value[0].problemSet.name

    fun onEvent(event: EvaluationEvent) {
        when (event) {
            is EvaluationEvent.SelectTabIndex ->
                tabIndex.value = event.index
        }
    }
}

sealed class EvaluationEvent() {
    class SelectTabIndex(val index: Int) : EvaluationEvent()
}