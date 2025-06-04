package mok.it.tortura.model

import kotlinx.serialization.Required
import kotlinx.serialization.Serializable

@Serializable
data class Student(
    val name: String = "",
    val group: String = "",
    val klass: String = "",
    @Required
    private val id: Int = getId()
) {
    companion object {
        private var nextId = 0
        fun getId() = nextId++
    }
}
