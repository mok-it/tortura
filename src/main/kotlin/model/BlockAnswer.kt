package model

class BlockAnswer( val block: Block ) {
    val answerHistory: MutableList<MutableMap<Task, SolutionState>> = mutableListOf()

    fun addBlockAttempt(){
        val newMap =  HashMap<Task, SolutionState>()
        for( task in block.tasks ){
            newMap[task] = SolutionState.EMPTY
        }
    }
}