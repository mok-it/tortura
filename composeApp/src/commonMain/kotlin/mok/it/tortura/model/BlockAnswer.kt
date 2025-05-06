package mok.it.tortura.model

import kotlinx.serialization.Serializable
import kotlin.math.pow

@Serializable
data class BlockAnswer(
    val block: Block,
    val answerHistory: List<Map<Task, SolutionState>> = listOf(),
    private val currentAnswersIndex: Int = -1,
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

    val restartEnabled
        get() = correctCount() != block.tasks.size

    val goNextEnabled
        get() = correctCount() >= block.minCorrectToProgress

    val canNavigateBackwards
        get() = currentAnswersIndex > 0

    val canNavigateForwards
        get() = currentAnswersIndex < answerHistory.size - 1

    fun navigateBackwards(): BlockAnswer {
        return this.copy(currentAnswersIndex = currentAnswersIndex - 1)
    }

    fun navigateForwards(): BlockAnswer {
        return this.copy(currentAnswersIndex = currentAnswersIndex + 1)
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