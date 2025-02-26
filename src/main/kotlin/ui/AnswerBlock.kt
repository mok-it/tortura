package ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
    onCheckSolutions: () -> Unit
) {
    Column {
        for( task in block.tasks ) {

            val solution = remember { mutableStateOf("") }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.border(1.dp, Color.Blue)
            ) {
                Text(
                    text ="${indexOffset + block.tasks.indexOf(task) + 1}.",
                    fontSize = 40.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.width(100.dp)
                )
                Text(
                    text = task.text,
                    minLines = 5,
                    maxLines = 5,
                    modifier = Modifier
                        .padding(10.dp,0.dp)
                        .width(300.dp)
                )
                TextField(
                    value = solution.value,
                    onValueChange = { solution.value = it },
                    modifier = Modifier.width(100.dp).padding(0.dp,0.dp,10.dp,0.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(25.dp))
        Button(
            onClick = onCheckSolutions,
            modifier = Modifier.align(Alignment.End)
        ){
            Text("Ellenőrzés")
        }

    }
}

@Composable
fun SolveBlockPreview() {

    val block = Block( listOf(
        Task("The path of the righteous man is beset on all sides by the\n" +
            "Inequities of the selfish and the tyranny of evil men\n" +
            "Blessed is he who, in the name of charity and good will\n" +
            "shepherds the weak through the valley of darkness\n" +
            "for he is truly his brother's keeper and the finder of lost children\n" +
            "And I will strike down upon thee with great vengeance and furious\n" +
            "Anger those who attempt to poison and destroy my brothers\n" +
            "And you will know\n" +
            "My name is the Lord when I lay my vengeance upon theeThe path of the righteous man is beset on all sides by the\n" +
            "Inequities of the selfish and the tyranny of evil men\n" +
            "Blessed is he who, in the name of charity and good will\n" +
            "shepherds the weak through the valley of darkness\n" +
            "for he is truly his brother's keeper and the finder of lost children\n" +
            "And I will strike down upon thee with great vengeance and furious\n" +
            "Anger those who attempt to poison and destroy my brothers\n" +
            "And you will know\n" +
            "My name is the Lord when I lay my vengeance upon thee", "25"), Task("Micimackó","17")
    ),1 )
    AnswerBlock( block, 3, {})
}
