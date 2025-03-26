package mok.it.tortura.feature.createProblemSet

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import mok.it.tortura.model.Block
import mok.it.tortura.model.ProblemSet
import mok.it.tortura.model.Task

class CreateTaskViewModel : ViewModel() {
    val problemSet = mutableStateOf<ProblemSet>(ProblemSet("", listOf()))


    private fun addTask(block: Block) {
        val blockIndex = problemSet.value.blocks.indexOf(block)
        val b = problemSet.value.blocks.filter { it != block }.toMutableList()
        b.add(
            blockIndex,
            block.copy(
                tasks = block.tasks + Task(
                    "",
                    ""
                )
            )
        )
        problemSet.value = problemSet.value.copy(blocks = b)
    }

    fun onEvent(event: CompetitionEditEvent) {
        when (event) {
            is CompetitionEditEvent.AddTask -> addTask(event.block)

            is CompetitionEditEvent.AddBlock -> problemSet.value =
                problemSet.value.copy(
                    blocks = problemSet.value.blocks + Block(listOf(), 0)
                )

            is CompetitionEditEvent.DeleteTask -> {
                println(event.task.text)
                val newTasks = event.block.tasks.filter { it != event.task }
                val newBlock = event.block.copy(tasks = newTasks)
                val blockIndex = problemSet.value.blocks.indexOf(event.block)
                val newBlocks = problemSet.value.blocks
                    .filter { it != event.block }
                    .toMutableList()
                newBlocks.add(blockIndex, newBlock)
                problemSet.value = problemSet.value.copy(blocks = newBlocks)
            }

            is CompetitionEditEvent.DeleteBlock -> problemSet.value =
                problemSet.value.copy(blocks = problemSet.value.blocks.filter { it != event.block })

            is CompetitionEditEvent.ChangeTaskText -> {
                val newTask = event.task.copy(text = event.text)
                val taskIndex = event.block.tasks.indexOf(event.task)
                val newTasks = event.block.tasks.filter { it != event.task }.toMutableList()
                newTasks.add(taskIndex, newTask)
                val newBlock = event.block.copy(tasks = newTasks)
                val blockIndex = problemSet.value.blocks.indexOf(event.block)
                val newBocks = problemSet.value.blocks.filter { it != event.block }.toMutableList()
                newBocks.add(blockIndex, newBlock)
                problemSet.value = problemSet.value.copy(blocks = newBocks)
            }

            is CompetitionEditEvent.ChangeTaskSolution -> {
                val newTask = event.task.copy(solution = event.text)
                val taskIndex = event.block.tasks.indexOf(event.task)
                val newTasks = event.block.tasks.filter { it != event.task }.toMutableList()
                newTasks.add(taskIndex, newTask)
                val newBlock = event.block.copy(tasks = newTasks)
                val blockIndex = problemSet.value.blocks.indexOf(event.block)
                val newBocks = problemSet.value.blocks.filter { it != event.block }.toMutableList()
                newBocks.add(blockIndex, newBlock)
                problemSet.value = problemSet.value.copy(blocks = newBocks)
            }

        }
    }

}

sealed class CompetitionEditEvent {
    data object AddBlock : CompetitionEditEvent()
    data class AddTask(val block: Block) : CompetitionEditEvent()
    data class DeleteBlock(val block: Block) : CompetitionEditEvent()
    data class DeleteTask(val task: Task, val block: Block) : CompetitionEditEvent()
    data class ChangeTaskText(val block: Block, val task: Task, val text: String) : CompetitionEditEvent()
    data class ChangeTaskSolution(val block: Block, val task: Task, val text: String) : CompetitionEditEvent()
}
