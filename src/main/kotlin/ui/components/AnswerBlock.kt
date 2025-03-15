package ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import model.Block
import model.SolutionState
import model.Task

@Composable
fun AnswerBlock(
    block: Block,
    teamName: String,
    indexOffset: Int = 0,
    modifyAnswer: (task: Task, newAnswer: SolutionState ) -> Unit,
    onCheckSolutions: () -> Unit,
    textColor: Color = Color.Unspecified,
    backgroundColor: Color = Color.Unspecified,
) {

//    val solution = remember { mutableStateOf(List(block.tasks.size) { "" }) }

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
    ){

        item {
            Spacer(Modifier.height(20.dp))
            Text(
                text = teamName,
                color = textColor,
                fontSize = 40.sp,
                modifier = Modifier.background( color = backgroundColor, shape = RoundedCornerShape(4.dp) )
                    .padding(5.dp),
            )
            Spacer(Modifier.height(20.dp))
        }

        for( i in 0..<block.tasks.size ) {

            val task = block.tasks[i]

            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.border(1.dp, if(backgroundColor == Color.Unspecified) Color.Blue else backgroundColor),
                ) {
                    Text(
                        text ="${indexOffset + block.tasks.indexOf(task) + 1}.",
                        fontSize = 40.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.width(100.dp),
                    )
                    Text(
                        text = task.text,
                        minLines = 5,
                        maxLines = 5,
                        modifier = Modifier
                            .padding(10.dp,0.dp)
                            .width(300.dp)
                    )
                    Text(
                        text = task.solution,
                        fontSize = 40.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.width(100.dp),
                    )
                    var answer by remember{ mutableStateOf(SolutionState.EMPTY) }
                    IconButton(
                        onClick = {
                            answer = if( answer != SolutionState.CORRECT ) SolutionState.CORRECT else SolutionState.EMPTY
                            modifyAnswer(task, answer)
                        },
                    ){
                        Icon( if(answer == SolutionState.CORRECT) Icons.Filled.CheckBox else Icons.Filled.Check, contentDescription = "", tint = Color.Green ) //TODO: szebb ikonok
                    }
                    IconButton(
                        onClick = {
                            answer = if( answer != SolutionState.INCORRECT ) SolutionState.INCORRECT else SolutionState.EMPTY
                            modifyAnswer(task, answer)
                        },
                    ){
                        Icon( if(answer == SolutionState.INCORRECT) Icons.Filled.Cancel else Icons.Filled.Close, contentDescription = "", tint = Color.Red ) //TODO: szebb ikonok
                    }
                }
            }

        }

        item {
            Spacer(modifier = Modifier.height(25.dp))
            Button(
                onClick = { onCheckSolutions() },
//                modifier = Modifier.fillMaxWidth().wrapContentWidth(Alignment.End),
                colors = buttonColors( backgroundColor = backgroundColor )
            ){
                Text(
                    text= "Ellenőrzés",
                    color= textColor,
                )
            }
        }


    }
}