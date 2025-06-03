package mok.it.tortura.ui.components

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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mok.it.tortura.model.BlockAnswer
import mok.it.tortura.model.SolutionState
import mok.it.tortura.model.Task

@Composable
fun AnswerBlock(
    teamName: String,
    answers: BlockAnswer,
    indexOffset: Int = 0,
    modifyAnswer: (task: Task, newAnswer: SolutionState) -> Unit = { _, _ -> },
    onRestartBlock: () -> Unit = {},
    onNextBlock: () -> Unit = {},
    onNavigateBackWards: () -> Unit = {},
    onNavigateForwards: () -> Unit = {},
    onDeleteLastTry: () -> Unit = {},
    navigateBackWardsEnabled: Boolean = false,
    navigateForwardsEnabled: Boolean = false,
    deleteLastTryEnabled: Boolean = false,
    textColor: Color = Color.Unspecified,
    backgroundColor: Color = Color.Unspecified,
    modifier: Modifier = Modifier,
) {

    val iconSize = 30.dp

    val currentAnswers = answers.currentAnswers

    val block = answers.block

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
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
                            .padding(10.dp, 0.dp)
                            .width(300.dp)
                    )
                    Text(
                        text = task.solution,
                        fontSize = 40.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.width(100.dp),
                    )
                    val answer = currentAnswers[ task ]
                    IconButton(
                        onClick = {
                            modifyAnswer(task, if( answer != SolutionState.CORRECT ) SolutionState.CORRECT else SolutionState.EMPTY)
                        },
                    ){
                        if( answer == SolutionState.CORRECT ) {
                            Icon(
                                Icons.Filled.Check,
                                contentDescription = "",
                                tint = Color.White,
                                modifier = Modifier
                                    .background(color = Color.Green, shape = RoundedCornerShape(iconSize / 2))
                                    .size(iconSize)
                            )
                        } else {
                            Icon(
                                Icons.Filled.Check,
                                contentDescription = "",
                                tint = Color.Green,
                                modifier = Modifier.size(iconSize)
                            )
                        }
                    }
                    IconButton(
                        onClick = {
                            modifyAnswer(task, if( answer != SolutionState.INCORRECT ) SolutionState.INCORRECT else SolutionState.EMPTY)
                        },
                    ){
                        if( answer == SolutionState.INCORRECT ) {
                            Icon(
                                Icons.Filled.Close,
                                contentDescription = "",
                                tint = Color.White,
                                modifier = Modifier
                                    .background(color = Color.Red, shape = RoundedCornerShape(iconSize / 2))
                                    .size(iconSize),
                            )
                        } else {
                            Icon(
                                Icons.Filled.Close,
                                contentDescription = "",
                                tint = Color.Red ,
                                modifier = Modifier.size(iconSize),
                            )
                        }
                    }
                }
            }

        }

        item {
            Spacer(modifier = Modifier.height(25.dp))
            Row {

                IconButton(
                    onClick = onDeleteLastTry,
                    enabled = deleteLastTryEnabled,
                ){
                    Icon(
                        Icons.Filled.DeleteForever,
                        contentDescription = null,
                        )
                }

                IconButton(
                    onClick = onNavigateBackWards,
                    enabled = navigateBackWardsEnabled,
                ){
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        null
                    )
                }

                Button(
                    onClick = onRestartBlock,
                    enabled = answers.restartEnabled,
                    colors = buttonColors( backgroundColor = backgroundColor ),
                ){
                    Text(
                        text= "Blokk újrakezdése",
                        color= textColor,
                    )
                }

                Button(
                    onClick = onNextBlock,
                    enabled = answers.goNextEnabled,
                ){
                    Text(
                        text = "Következő blokk",
                        color= textColor,
                    )
                }

                IconButton(
                    onClick = onNavigateForwards,
                    enabled = navigateForwardsEnabled,
                ){
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "", )
                }
            }
        }


    }
}