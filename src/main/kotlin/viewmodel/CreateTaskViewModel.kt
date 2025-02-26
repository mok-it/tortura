package viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import model.Block
import model.Competition
import model.Task

class CreateTaskViewModel : ViewModel() {
    val competition = mutableStateOf<Competition>(Competition("", listOf()))


    private fun addTask(block: Block) {
        val blockIndex = competition.value.blocks.indexOf(block)
        val b = competition.value.blocks.filter { it != block }.toMutableList()
        b.add(
            blockIndex,
            block.copy(
                tasks = block.tasks + Task(
                    "",
                    ""
                )
            )
        )
        competition.value = competition.value.copy(blocks = b)
    }

    fun onEvent(event: CompetitionEditEvent) {
        when (event) {
            is CompetitionEditEvent.AddTask -> addTask(event.block)

            is CompetitionEditEvent.AddBlock -> competition.value =
                competition.value.copy(
                    blocks = competition.value.blocks + Block(listOf(), 0)
                )

            is CompetitionEditEvent.DeleteTask -> {
                println(event.task.text)
                val newTasks = event.block.tasks.filter { it != event.task }
                val newBlock = event.block.copy(tasks = newTasks)
                val blockIndex = competition.value.blocks.indexOf(event.block)
                val newBlocks = competition.value.blocks
                    .filter { it != event.block }
                    .toMutableList()
                newBlocks.add(blockIndex, newBlock)
                competition.value = competition.value.copy(blocks = newBlocks)
            }

            is CompetitionEditEvent.DeleteBlock -> competition.value =
                competition.value.copy(blocks = competition.value.blocks.filter { it != event.block })

            is CompetitionEditEvent.ChangeTaskText -> {
                val newTask = event.task.copy(text = event.text)
                val taskIndex = event.block.tasks.indexOf(event.task)
                val newTasks = event.block.tasks.filter { it != event.task }.toMutableList()
                newTasks.add(taskIndex, newTask)
                val newBlock = event.block.copy(tasks = newTasks)
                val blockIndex = competition.value.blocks.indexOf(event.block)
                val newBocks = competition.value.blocks.filter { it != event.block }.toMutableList()
                newBocks.add(blockIndex, newBlock)
                competition.value = competition.value.copy(blocks = newBocks)
            }

            is CompetitionEditEvent.ChangeTaskSolution -> {
                val newTask = event.task.copy(solution = event.text)
                val taskIndex = event.block.tasks.indexOf(event.task)
                val newTasks = event.block.tasks.filter { it != event.task }.toMutableList()
                newTasks.add(taskIndex, newTask)
                val newBlock = event.block.copy(tasks = newTasks)
                val blockIndex = competition.value.blocks.indexOf(event.block)
                val newBocks = competition.value.blocks.filter { it != event.block }.toMutableList()
                newBocks.add(blockIndex, newBlock)
                competition.value = competition.value.copy(blocks = newBocks)
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
