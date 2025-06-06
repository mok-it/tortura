package mok.it.tortura.model

import kotlinx.serialization.Serializable
import kotlin.math.pow

@Serializable
data class BlockAnswer(
    val block: Block,
    val answerHistory: List<Map<Task, SolutionState>> = listOf(),
    val currentAnswersIndex: Int = -1,
) {

    val currentAnswers
        get() = answerHistory[currentAnswersIndex]

    fun addBlockAttempt(): BlockAnswer {
        val newMap = HashMap<Task, SolutionState>()
        for (task in block.tasks) {
            newMap[task] = SolutionState.EMPTY
        }
        return this.copy(answerHistory = answerHistory.plus(newMap), currentAnswersIndex = currentAnswersIndex + 1)
    }

    fun changeAnswer(task: Task, newAnswer: SolutionState): BlockAnswer {
        val newCurrentAnswers = currentAnswers.toMutableMap()
        newCurrentAnswers[task] = newAnswer
        val newAnswerHistory = answerHistory.toMutableList()
        newAnswerHistory.removeAt(currentAnswersIndex)
        newAnswerHistory.add(currentAnswersIndex,newCurrentAnswers)
        return this.copy(answerHistory = newAnswerHistory)
    }

    val allTaskAnswered: Boolean
        get() {
            for( task in block.tasks ){
                if( currentAnswers[ task ] == SolutionState.EMPTY){
                    return false
                }
            }
            return true
        }

    val lastIsSelected
        get() = currentAnswersIndex == answerHistory.size - 1

    val restartEnabled
        get() = lastIsSelected && allTaskAnswered && correctCount() != block.tasks.size

    val goNextEnabled
        get() = lastIsSelected && allTaskAnswered && correctCount() >= block.minCorrectToProgress

    val canNavigateBackwards
        get() = (lastIsSelected || allTaskAnswered) && currentAnswersIndex > 0

    val canNavigateForwards
        get() = allTaskAnswered && currentAnswersIndex < answerHistory.size - 1

    val canDeleteLastTry
        get() = currentAnswersIndex == answerHistory.size - 1 && canNavigateBackwards

    fun navigateBackwards(): BlockAnswer {
        return this.copy(currentAnswersIndex = currentAnswersIndex - 1)
    }

    fun navigateForwards(): BlockAnswer {
        return this.copy(currentAnswersIndex = currentAnswersIndex + 1)
    }

    fun deleteLastTry(): BlockAnswer{
        val newAnswerHistory = answerHistory.subList( 0, answerHistory.size - 1 )
        return this.copy(answerHistory = newAnswerHistory, currentAnswersIndex = newAnswerHistory.size - 1)
    }

    fun correctCount(): Int {
        var count = 0
        for (answer in answerHistory.last()) {
            if (answer.value == SolutionState.CORRECT) {
                count++
            }
        }
        return count
    }

    fun points(base: Double): Double {
        var sum = 0.0
        for (task in block.tasks) {
            if (answerHistory.last()[task] == SolutionState.CORRECT) {
                sum += base / 2.0.pow(lastCorrect(task))
            }
        }
        return sum
    }

    private fun lastCorrect(task: Task): Int {
        var result = -1
        for (i in answerHistory.indices) {
            if (answerHistory[i][task] == SolutionState.CORRECT && result == -1) {
                result = i
            }
        }
        return result
    }
}