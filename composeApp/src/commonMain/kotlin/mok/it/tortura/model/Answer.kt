package mok.it.tortura.model

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class Answer(
    val problemSet: ProblemSet,
    val answerHistory: List<BlockAnswer> = listOf(BlockAnswer(problemSet.blocks.first()).addBlockAttempt()),
    val currentBlockAnswerIndex: Int = 0,
    val blockIdx: Int = 0,
    val finished: Boolean = false,
    val lastAnswerTime: Instant = Clock.System.now(),
) {

    val currentBlockAnswer: BlockAnswer
        get() = answerHistory[currentBlockAnswerIndex]


    fun answerTask(task: Task, newAnswer: SolutionState): Answer {
        val newBlockAnswer = currentBlockAnswer.changeAnswer(task, newAnswer)
        val newList = answerHistory.toMutableList()
        newList.removeAt(currentBlockAnswerIndex)
        newList.add(currentBlockAnswerIndex,newBlockAnswer)
        return this.copy(answerHistory = newList, lastAnswerTime = Clock.System.now())
    }

    fun restartCurrentBlock(): Answer {
        val newBlockAnswer = answerHistory.last().addBlockAttempt()
        val newList = answerHistory.toMutableList()
        newList.removeLast()
        newList.add(newBlockAnswer)
        return this.copy(answerHistory = newList )
    }

    fun nextBlock(): Answer {
        if (blockIdx == problemSet.blocks.size - 1) {
            return this.copy(finished = true)
        }
        val newAnswerHistory = answerHistory.toMutableList()
        newAnswerHistory.add(BlockAnswer(problemSet.blocks[blockIdx + 1]).addBlockAttempt())
        return this.copy(answerHistory = newAnswerHistory, blockIdx = blockIdx + 1, currentBlockAnswerIndex = currentBlockAnswerIndex + 1)
    }

    val lastIsSelected
        get() = currentBlockAnswerIndex == answerHistory.size - 1

    val canNavigateBackward
        get() = (currentBlockAnswer.canNavigateBackwards || currentBlockAnswerIndex > 0) && ( lastIsSelected || currentBlockAnswer.allTaskAnswered )

    val canNavigateForward
        get() =( currentBlockAnswer.canNavigateForwards || currentBlockAnswerIndex < answerHistory.size - 1) && currentBlockAnswer.allTaskAnswered

    val canDeleteLastTry
        get() = currentBlockAnswer.canDeleteLastTry || (currentBlockAnswerIndex == answerHistory.size - 1 && canNavigateBackward )

    val restartEnabled
        get() = lastIsSelected && currentBlockAnswer.restartEnabled

    val goNextEnabled
        get() = lastIsSelected && currentBlockAnswer.goNextEnabled

    fun navigateBackwards(): Answer {
        if( finished ){
            return this.copy( finished = false )
        }
        if( currentBlockAnswer.canNavigateBackwards ) {
            val newAnswerHistory = answerHistory.toMutableList()
            val newCurrentBlockAnswer = currentBlockAnswer.navigateBackwards()
            newAnswerHistory.removeAt(currentBlockAnswerIndex)
            newAnswerHistory.add(currentBlockAnswerIndex, newCurrentBlockAnswer)
            return this.copy(answerHistory = newAnswerHistory)
        }
        return this.copy( currentBlockAnswerIndex = currentBlockAnswerIndex - 1 )
    }

    fun navigateForwards(): Answer {
        if( currentBlockAnswer.canNavigateForwards ) {
            val newAnswerHistory = answerHistory.toMutableList()
            val newCurrentBlockAnswer = currentBlockAnswer.navigateForwards()
            newAnswerHistory.removeAt(currentBlockAnswerIndex)
            newAnswerHistory.add(currentBlockAnswerIndex, newCurrentBlockAnswer)
            return this.copy(answerHistory = newAnswerHistory)
        }
        return this.copy(currentBlockAnswerIndex = currentBlockAnswerIndex + 1)
    }

    fun deleteLastTry(): Answer{
        if( answerHistory.last().canDeleteLastTry ){
            val newLast = answerHistory.last().deleteLastTry()
            val newAnswerHistory= answerHistory.toMutableList()
            newAnswerHistory.removeLast()
            newAnswerHistory.add(newLast)
            return this.copy(answerHistory = newAnswerHistory)
        }
        val newAnswerHistory = answerHistory.subList(0, answerHistory.size - 1)
        return this.copy(answerHistory = newAnswerHistory, currentBlockAnswerIndex = newAnswerHistory.size - 1, blockIdx = blockIdx - 1)
    }

    fun points(): Double {
        var sum = 0.0

        var base = 32.0

        for (blockAnswer in answerHistory) {
            sum += blockAnswer.points(base)
            base += blockAnswer.correctCount() * 8.0
        }

        return sum
    }

}
