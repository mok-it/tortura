package model

data class Answer( val problemSet: ProblemSet, ){
    val answerHistory: MutableList<BlockAnswer> = mutableListOf( BlockAnswer(problemSet.blocks.first()) )

    var blockIdx = 0

    fun getCurrentBlock() = problemSet.blocks[blockIdx]

}
