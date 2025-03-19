package model

data class Answer(
    val problemSet: ProblemSet,
    val answerHistory: List<BlockAnswer> = listOf( BlockAnswer(problemSet.blocks.first()).addBlockAttempt() ),
    val blockIdx: Int = 0
){

    fun getCurrentBlock() = problemSet.blocks[blockIdx]

    fun answerTask( task: Task, newAnswer: SolutionState ): Answer{
        val newBlockAnswer = answerHistory.last().changeAnswer(task, newAnswer)
        val newList = answerHistory.toMutableList()
        newList.removeLast()
        newList.add(newBlockAnswer)
        return this.copy(answerHistory = newList)
    }

}
