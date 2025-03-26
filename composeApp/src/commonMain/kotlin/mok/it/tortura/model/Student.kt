package mok.it.tortura.model

import kotlinx.serialization.Serializable

@Serializable
data class Student(
    val name: String = "",
    val group: String = "",
    val klass: String = "",
    val id: Int = getId()
) {
    companion object {
        private var nextId = 0
        fun getId() = nextId++
    }
}
