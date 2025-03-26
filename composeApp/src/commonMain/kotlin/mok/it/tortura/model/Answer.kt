package model

data class Answer(
    val problemSet: ProblemSet,
    val answerHistory: List<BlockAnswer> = listOf( BlockAnswer(problemSet.blocks.first()).addBlockAttempt() ),
    val blockIdx: Int = 0,
    val finished: Boolean = false
){

    fun getCurrentBlock() = problemSet.blocks[blockIdx]

    fun answerTask( task: Task, newAnswer: SolutionState ): Answer{
        val newBlockAnswer = answerHistory.last().changeAnswer(task, newAnswer)
        val newList = answerHistory.toMutableList()
        newList.removeLast()
        newList.add(newBlockAnswer)
        return this.copy(answerHistory = newList)
    }

    fun restartCurrentBlock() : Answer{
        val newBlockAnswer = answerHistory.last().addBlockAttempt()
        val newList = answerHistory.toMutableList()
        newList.removeLast()
        newList.add(newBlockAnswer)
        return this.copy(answerHistory = newList)
    }

    fun nextBlock() : Answer{
        if( blockIdx == problemSet.blocks.size - 1 ){
            return this.copy(finished = true)
        }
        val newAnswerHistory = answerHistory.toMutableList()
        newAnswerHistory.add( BlockAnswer(problemSet.blocks[ blockIdx + 1 ]).addBlockAttempt() )
        return this.copy(answerHistory = newAnswerHistory, blockIdx = blockIdx + 1 )
    }


}
