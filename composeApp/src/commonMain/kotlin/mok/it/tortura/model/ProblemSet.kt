package mok.it.tortura.model

import kotlinx.serialization.Serializable

@Serializable
data class ProblemSet(val name: String, val blocks: List<Block>, val category: String = ""){

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

}