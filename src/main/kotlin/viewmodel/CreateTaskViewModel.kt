package viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import model.Block
import model.Task

class CreateTaskViewModel : ViewModel() {
    //var _blocks = mutableStateListOf(Block(emptyList(), 0))
    val blocks = mutableStateListOf(Block(emptyList(), 0))

    fun deleteTask(task: Task, block: Block) {
        blocks.remove(block)
        val b = block.copy(tasks = block.tasks.filter { t -> t != task })
        blocks.add(b)
    }

    fun deleteBlock(block: Block) {
        blocks.remove(block)
    }

    fun newBlock() {
        blocks.add(Block(listOf(Task("", "")), 0))
    }

    fun addTask(block: Block) {
        blocks.remove(block)
        blocks.add(block.copy(tasks = block.tasks + Task("", "")))
    }
}