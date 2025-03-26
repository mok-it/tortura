package mok.it.tortura.model

import kotlinx.serialization.Serializable

@Serializable
data class Block(
    val tasks: List<Task>,
    val minCorrectToProgress: Int,
    val id: Int = nextId()
) {
    companion object IdCounter {
        private var id = 0
        fun nextId(): Int {
            id += 1
            return id
        }
    }
}
