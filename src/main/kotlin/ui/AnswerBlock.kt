package ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import model.Block
import model.Task

@Composable
fun AnswerBlock(
    block: Block,
    indexOffset: Int = 0,
    modifyAnswer: (task: Task, newAnswer: Boolean? ) -> Unit,
    onCheckSolutions: () -> Unit,
    textColor: Color = Color.Unspecified,
    backgroundColor: Color = Color.Unspecified,
) {

//    val solution = remember { mutableStateOf(List(block.tasks.size) { "" }) }

    Column{
        for( i in 0..<block.tasks.size ) {

            val task = block.tasks[i]

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
                var answer by remember{ mutableStateOf(false) }
                IconButton(
                    onClick = {
                        modifyAnswer(task, true )
                        answer = true
                              },
                    ){
                    Icon( if(answer) Icons.Filled.CheckCircle else Icons.Filled.Check, contentDescription = "", tint = Color.Green )
                }
                IconButton(
                    onClick = {
                        modifyAnswer(task, false )
                        answer = false
                    },
                ){
                    Icon( if(!answer) Icons.Filled.AccountBox else Icons.Filled.Close, contentDescription = "", tint = Color.Red )
                }
//                TextField(
//                    value = solution.value[i],
//                    onValueChange = {
//                                        val list = solution.value.toMutableList()
//                                        list[i] = it
//                                        solution.value = list
//                                    },
//                    modifier = Modifier.width(100.dp).padding(0.dp,0.dp,10.dp,0.dp)
//                )
            }
        }
        Spacer(modifier = Modifier.height(25.dp))
        Button(
            onClick = { onCheckSolutions() },
            modifier = Modifier.align(Alignment.End),
            colors = buttonColors( backgroundColor = backgroundColor )
        ){
            Text(text= "Ellenőrzés", color= textColor)
        }

    }
}