package model

data class ProblemSet(val name: String, val blocks: List<Block>, val category: String = "") {

    fun getTotalTasks(): Int {
        var sum = 0
        for (block in blocks) {
            sum += block.getTotalTasks()
        }
        return sum
    }

    fun getAllTasks(): List<Task> {
        val tasks = mutableListOf<Task>()
        for (block in blocks) {
            tasks.addAll(block.tasks)
        }
        return tasks
    }
}