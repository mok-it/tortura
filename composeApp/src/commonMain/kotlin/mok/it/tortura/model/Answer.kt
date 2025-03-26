package mok.it.tortura.model

data class Answer(
    val problemSet: ProblemSet,
    val answerHistory: List<BlockAnswer> = listOf( BlockAnswer(problemSet.blocks.first()).addBlockAttempt() ),
    val blockIdx: Int = 0,
    val finished: Boolean = false
){

    fun getCurrentBlock() = problemSet.blocks[blockIdx]

    fun answerTask(task: Task, newAnswer: SolutionState): Answer {
        val newBlockAnswer = answerHistory.last().changeAnswer(task, newAnswer)
        val newList = answerHistory.toMutableList()
        newList.removeLast()
        newList.add(newBlockAnswer)
        return this.copy(answerHistory = newList)
    }

    fun restartCurrentBlock() : Answer {
        val newBlockAnswer = answerHistory.last().addBlockAttempt()
        val newList = answerHistory.toMutableList()
        newList.removeLast()
        newList.add(newBlockAnswer)
        return this.copy(answerHistory = newList)
    }

    fun nextBlock() : Answer {
        if( blockIdx == problemSet.blocks.size - 1 ){
            return this.copy(finished = true)
        }
        val newAnswerHistory = answerHistory.toMutableList()
        newAnswerHistory.add( BlockAnswer(problemSet.blocks[ blockIdx + 1 ]).addBlockAttempt() )
        return this.copy(answerHistory = newAnswerHistory, blockIdx = blockIdx + 1 )
    }

    fun points() : Double{
        var sum = 0.0

        var base = 32.0

        for( blockAnswer in answerHistory ){
            sum += blockAnswer.points( base )
            base += blockAnswer.correctCount() * 8.0
        }

        return sum
    }


//    fun calculatePoints(): Double {
//        var points = 0.0
//
//        var base = 32
//        var baseIncrement = 0
//
//        for (block in competition.blocks) {
//            base += baseIncrement
//            baseIncrement = 0
//            for (task in block.tasks) {
//                val answers = answerHistory[task] ?: continue
//                var lastCorrect = -1
//                for ((i, answer) in answers.withIndex()) {
//                    if (answer != task.solution) {
//                        lastCorrect = -1
//                    } else if (lastCorrect == -1) {
//                        lastCorrect = i
//                    }
//                }
//                if (lastCorrect != -1) {
//                    points += base / 2.0.pow(lastCorrect)
//                    baseIncrement += 8
//                }
//            }
//        }
//        return points
//    }

}
