package model

data class Block(
    val tasks: List<Task>,
    val minCorrectToProgress: Int,
    val id: Int = nextId()
) {
    fun getTotalTasks() = tasks.size

    private fun correctCount(answers: List<String>): Int {
        var correct = 0
        for ((t, a) in tasks.zip(answers)) {
            if (t.solution == a) {
                correct++
            }
        }
        return correct
    }

    fun canProgress(answers: List<String>): Boolean {
        return correctCount(answers) >= minCorrectToProgress
    }

    fun mustProgress(answers: List<String>): Boolean {
        return correctCount(answers) == tasks.size
    }

    companion object IdCounter {
        private var id = 0
        fun nextId(): Int {
            id += 1
            return id
        }
    }
}
