package model

data class Team(
    val students: MutableList<Student>,
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
