package mok.it.tortura.ui.components

import CorrectIcon
import IncorrectIcon
import NavigateBackIcon
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mok.it.tortura.model.Answer
import mok.it.tortura.model.SolutionState

@Composable
fun FinishedContent(
    textColor: Color,
    backgroundColor: Color,
    points: String,
    answer: Answer,
    onNavigateBack: () -> Unit,
    modifier: Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier
    ) {
        Text(
            text = "Végeztek",
            fontSize = 50.sp,
            color = textColor,
            modifier = Modifier.background(
                color = backgroundColor,
                shape = RoundedCornerShape(4.dp)
            )
        )

        Text(
            text = "Pontok: $points",
            fontSize = 40.sp,
            color = textColor,
            modifier = Modifier.background(
                color = backgroundColor,
                shape = RoundedCornerShape(4.dp)
            )
        )

        val answerShown = remember { mutableStateOf(false) }

        val maxTasks = answer.problemSet.maxTasks

        if( answerShown.value ) {
            LazyColumn(
                modifier = Modifier.weight(1f),
            ) {
                for (index in answer.answerHistory.indices) {
                    val blockAnswer = answer.answerHistory[index]
                    val block = blockAnswer.block
                    for (answerMap in blockAnswer.answerHistory) {
                        item {
                            Card(
                                modifier = Modifier.padding(5.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp)
                                ) {
                                    Text(
                                        text = "${index + 1}.",
                                        modifier = Modifier.padding(8.dp),
                                    )
                                    for (task in block.tasks) {
                                        val solutionState = answerMap[task]!!
                                        if (solutionState == SolutionState.CORRECT) {
                                            CorrectIcon(
                                                selected = true,
                                                iconSize = 30.dp,
                                                modifier = Modifier.padding(4.dp)
                                            )
                                        } else {
                                            IncorrectIcon(
                                                selected = true,
                                                iconSize = 30.dp,
                                                modifier = Modifier.padding(4.dp)
                                            )
                                        }
                                    }
                                    for( i in 0 ..< maxTasks - block.tasks.size ) {
                                        Spacer( modifier = Modifier.padding(4.dp).size(30.dp) )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Button(
            onClick = { answerShown.value = !answerShown.value },
            modifier = Modifier.padding(10.dp)
        ) {
            Text(
                text = if (answerShown.value) "Válaszok elrejtése" else "Válaszok megjelenítése",
                color = textColor,
                fontSize = 20.sp
            )
        }

        IconButton(
            onClick = onNavigateBack,
        ) {
            NavigateBackIcon()
        }
    }
}