package mok.it.tortura.model

import kotlinx.serialization.Serializable
import kotlin.math.pow

@Serializable
data class BlockAnswer(val block: Block, val answerHistory: List<Map<Task, SolutionState>> = listOf()) {

    fun addBlockAttempt(): BlockAnswer {
        val newMap = HashMap<Task, SolutionState>()
        for (task in block.tasks) {
            newMap[task] = SolutionState.EMPTY
        }
        return this.copy(answerHistory = answerHistory.plus(newMap))
    }

    fun changeAnswer(task: Task, newAnswer: SolutionState): BlockAnswer {
        val currentAnswers = answerHistory.last().toMutableMap()
        currentAnswers[task] = newAnswer
        val newAnswerHistory = answerHistory.toMutableList()
        newAnswerHistory.removeLast()
        newAnswerHistory.add(currentAnswers)
        return this.copy(answerHistory = newAnswerHistory)
    }

    fun restartEnabled(): Boolean = correctCount() != block.tasks.size

    fun goNextEnabled(): Boolean = correctCount() >= block.minCorrectToProgress

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