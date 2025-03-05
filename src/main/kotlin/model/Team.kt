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

//TODO: should be in a different class

//    fun calculatePoints(): Double {
//        var points = 0.0
//
//        var base = 32
//        var baseIncrement = 0
//
//        for (block in competition.blocks) {
//            base += baseIncrement
//            baseIncrement = 0
//            for (task in block.tasks) {
//                val answers = answerHistory[task] ?: continue
//                var lastCorrect = -1
//                for ((i, answer) in answers.withIndex()) {
//                    if (answer != task.solution) {
//                        lastCorrect = -1
//                    } else if (lastCorrect == -1) {
//                        lastCorrect = i
//                    }
//                }
//                if (lastCorrect != -1) {
//                    points += base / 2.0.pow(lastCorrect)
//                    baseIncrement += 8
//                }
//            }
//        }
//        return points
//    }
