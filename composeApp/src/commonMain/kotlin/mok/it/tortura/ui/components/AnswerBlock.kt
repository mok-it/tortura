package mok.it.tortura.ui.components

import CorrectIcon
import DeleteIcon
import IncorrectIcon
import NavigateBackIcon
import NavigateForwardIcon
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mok.it.tortura.model.Answer
import mok.it.tortura.model.SolutionState
import mok.it.tortura.model.Task

@Composable
fun AnswerBlock(
    teamName: String,
    answer: Answer,
    modifyAnswer: (task: Task, newAnswer: SolutionState) -> Unit = { _, _ -> },
    onRestartBlock: () -> Unit = {},
    onNextBlock: () -> Unit = {},
    onNavigateBackWards: () -> Unit = {},
    onNavigateForwards: () -> Unit = {},
    onDeleteLastTry: () -> Unit = {},
    textColor: Color = Color.Unspecified,
    backgroundColor: Color = Color.Unspecified,
    modifier: Modifier = Modifier,
) {

    val iconSize = 30.dp

    val currentAnswers = answer.currentBlockAnswer.currentAnswers

    val block = answer.currentBlockAnswer.block

    val indexOffset = answer.problemSet.previousTaskNumber( block )

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
            Spacer( Modifier.height(5.dp) )
            Text(
                text = "${answer.currentBlockAnswerIndex + 1}. block - ${answer.currentBlockAnswer.currentAnswersIndex + 1}. próbálkozás",
                color = textColor,
                fontSize = 20.sp,
                modifier = Modifier.background( color = backgroundColor, shape = RoundedCornerShape( 2.dp) )
                    .padding(3.dp),
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
                        CorrectIcon(
                            selected = answer == SolutionState.CORRECT,
                            iconSize = iconSize,
                        )
                    }
                    IconButton(
                        onClick = {
                            modifyAnswer(task, if( answer != SolutionState.INCORRECT ) SolutionState.INCORRECT else SolutionState.EMPTY)
                        },
                    ){
                        IncorrectIcon(
                            selected = answer == SolutionState.INCORRECT,
                            iconSize = iconSize,
                        )
                    }
                }
            }

        }

        item {
            Spacer(modifier = Modifier.height(25.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
            ) {

                IconButton(
                    onClick = onDeleteLastTry,
                    enabled = answer.canDeleteLastTry,
                ){
                    DeleteIcon()
                }

                IconButton(
                    onClick = onNavigateBackWards,
                    enabled = answer.canNavigateBackward,
                ){
                    NavigateBackIcon()
                }

                Button(
                    onClick = onRestartBlock,
                    enabled = answer.restartEnabled,
                    colors = buttonColors(
                        containerColor = backgroundColor,
                        contentColor = textColor,
                    ),
                    modifier = Modifier.padding(5.dp)
                ){
                    Text(
                        text= "Blokk újrakezdése",
                    )
                }

                Button(
                    onClick = onNextBlock,
                    enabled = answer.goNextEnabled,
                    colors = buttonColors(containerColor = backgroundColor ),
                    modifier = Modifier.padding(5.dp)
                ){
                    Text(
                        text = "Következő blokk",
                        color= textColor,
                    )
                }

                IconButton(
                    onClick = onNavigateForwards,
                    enabled = answer.canNavigateForward,
                ){
                    NavigateForwardIcon()
                }
            }
        }
    }
}