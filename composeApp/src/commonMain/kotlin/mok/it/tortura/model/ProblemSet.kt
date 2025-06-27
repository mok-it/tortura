package mok.it.tortura.model

import kotlinx.serialization.Serializable

@Serializable
data class ProblemSet(
    val blocks: List<Block>,
){

    fun previousTaskNumber( block: Block ): Int {
        var sum = 0
        for( b in blocks ){
            if( block == b ){
                return sum
            }
            sum += b.tasks.size
        }
        return sum
    }

    val maxTasks: Int
        get() {
            var max = 0
            for( block in blocks ){
                max = if( block.tasks.size > max ) block.tasks.size else max
            }
            return max
        }

}