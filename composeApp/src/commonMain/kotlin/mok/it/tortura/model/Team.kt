package mok.it.tortura.model

import kotlinx.serialization.Serializable

@Serializable
data class Team(
    val students: List<Student>,
    private val id: Int = nextId(),
) {
    companion object IdCounter {
        private var id = 0
        fun nextId(): Int {
            id += 1
            return id
        }
    }
}
