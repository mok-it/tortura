package mok.it.tortura.model

data class Task(
    val text: String = "",
    val solution: String,
    private val id: Int = nextId()
) {
    companion object IdCounter {
        private var id = 0
        fun nextId(): Int {
            id += 1
            return id
        }
    }
}