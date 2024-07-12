package model

import kotlin.math.pow

class Team(val id: Int, val students: MutableList<Student>, val competition: Competition) {
    private var currentBlock = competition.blocks[0]
    private var currentTask = currentBlock.tasks[0]

    //private val answersHistory = mutableListOf<MutableList<String>>()
    private val answerHistory: MutableMap<Task, MutableList<String>> = mutableMapOf()

    init {
        for (task in competition.getAllTasks()) {
            answerHistory[task] = mutableListOf()
        }
    }

    fun answer(answer: String) {
        answerHistory[currentTask]?.add(answer) ?: throw RuntimeException("Task does not appear in history as key")
        val index = currentBlock.tasks.indexOf(currentTask)
        if (index == currentBlock.tasks.size - 1) {
            evaluate()
        } else {
            currentTask = currentBlock.tasks[index + 1]
        }
    }

    fun evaluate() {
        val answers = currentBlock.tasks.map {
            answerHistory[it]?.last()
                ?: throw RuntimeException("Tried to evaluate before giving at least 1 answer for each task")
        }
        if (answers.size != currentBlock.tasks.size) {
            throw RuntimeException("Expected ${currentBlock.tasks.size} answers, got ${answers.size}")
        }
        if (currentBlock.mustProgress(answers)) {

        } else if (currentBlock.canProgress(answers)) {
            TODO("progress prompt")
        }
    }

    fun calculatePoints(): Double {
        var points = 0.0

        var base = 32
        var baseIncrement = 0

        for (block in competition.blocks) {
            base += baseIncrement
            baseIncrement = 0
            for (task in block.tasks) {
                val answers = answerHistory[task] ?: continue
                var lastCorrect = -1
                for ((i, answer) in answers.withIndex()) {
                    if (answer != task.solution) {
                        lastCorrect = -1
                    } else if (lastCorrect == -1) {
                        lastCorrect = i
                    }
                }
                if (lastCorrect != -1) {
                    points += base / 2.0.pow(lastCorrect)
                    baseIncrement += 8
                }
            }
        }
        return points
    }
}