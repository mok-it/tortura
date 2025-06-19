package mok.it.tortura.feature.createProblemSet

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.readString
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import mok.it.tortura.loadProblemSetFromExcel
import mok.it.tortura.model.Block
import mok.it.tortura.model.ProblemSet
import mok.it.tortura.model.Task

class CreateProblemSetViewModel : ViewModel() {
    val problemSet = mutableStateOf(ProblemSet("", listOf()))


    private fun modifyAllBlocks( newValue: List<Block>) {
        problemSet.value = problemSet.value.copy(blocks = newValue)
    }

    private fun modifySingleBlock(block: Block, newValue: Block) {
        val blockIndex = problemSet.value.blocks.indexOf(block)
        val newBlocks = problemSet.value.blocks.toMutableList()
        newBlocks[blockIndex] = newValue.copy( minCorrectToProgress = (newValue.tasks.size + 2) / 2 )
        modifyAllBlocks(newBlocks)
    }

    private fun modifyTask(block: Block, task: Task, newValue: Task) {
        val taskIndex = block.tasks.indexOf(task)
        val newTasks = block.tasks.toMutableList()
        newTasks[taskIndex] = newValue
        modifySingleBlock( block, block.copy( tasks = newTasks ) )
    }

    fun onEvent(event: CompetitionEditEvent) {
        when (event) {
            is CompetitionEditEvent.AddTask -> {
                modifySingleBlock( event.block, event.block.copy( tasks = event.block.tasks + Task() ) )
            }

            is CompetitionEditEvent.AddBlock -> {
                modifyAllBlocks( problemSet.value.blocks + Block() )
            }

            is CompetitionEditEvent.DeleteTask -> {
                modifySingleBlock( event.block, event.block.copy( tasks = event.block.tasks.filter { it != event.task } ) )
            }

            is CompetitionEditEvent.DeleteBlock -> {
                modifyAllBlocks( problemSet.value.blocks.filter { it != event.block } )
            }


            is CompetitionEditEvent.ChangeTaskText -> {
                modifyTask( event.block, event.task, event.task.copy(text = event.text) )
            }

            is CompetitionEditEvent.ChangeTaskSolution -> {
                modifyTask( event.block, event.task, event.task.copy( solution = event.text ) )
            }

            is CompetitionEditEvent.ChangeProblemSetName -> {
                problemSet.value = problemSet.value.copy(name = event.name)
            }

            is CompetitionEditEvent.ImportProblemSetFromJson -> {
                viewModelScope.launch {
                    try {
                        val newProblemSet = Json.decodeFromString<ProblemSet>(event.file.readString())
                        problemSet.value = newProblemSet
                    } catch (e: Exception) {
                        TODO()
                    }
                }

            }

            is CompetitionEditEvent.ImportProblemSetFromExcel -> {
                val newProblemSet = loadProblemSetFromExcel(event.file)
                if( newProblemSet != null ){
                    problemSet.value = newProblemSet
                }
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
    data class ChangeProblemSetName( val name: String ) : CompetitionEditEvent()
    data class ImportProblemSetFromJson(val file: PlatformFile) : CompetitionEditEvent()
    data class ImportProblemSetFromExcel(val file: PlatformFile) : CompetitionEditEvent()
}
