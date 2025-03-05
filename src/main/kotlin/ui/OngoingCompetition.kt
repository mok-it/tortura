package ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import viewmodel.OnGoingCompetitionEvent
import viewmodel.OngoingCompetitionViewModel

@Composable
fun OngoingCompetition() {

    val viewModel: OngoingCompetitionViewModel = viewModel()

    var tabIndex by remember { mutableStateOf(0) }

    val tabs = viewModel.teams.map { team -> viewModel.teams.indexOf(team).toString() }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        for (i in tabs.indices) {
            val colors = when (i) {
                in 0..3 -> CategoryColors.BOCS
                in 4..6 -> CategoryColors.KIS
                in 7..9 -> CategoryColors.NAGY
                else -> CategoryColors.JEGES
            }

            if (i == tabIndex) {
                AnswerBlock(
                    //viewModel.teams[i].competition.blocks[0],
                    viewModel.block,
                    0,
                    { list ->
                        viewModel.onEvent(OnGoingCompetitionEvent.SubmitSolution(viewModel.teams[i], list))
                    },
                    textColor = colors.textColor,
                    backgroundColor = colors.backgroundColor,
                )
            }
        }
        ScrollableTabRow(
            selectedTabIndex = tabIndex,
            backgroundColor = Color.Transparent,
        ) {
            tabs.forEachIndexed { index, title ->

                val colors = when (index) {
                    in 0..3 -> CategoryColors.BOCS
                    in 4..6 -> CategoryColors.KIS
                    in 7..9 -> CategoryColors.NAGY
                    else -> CategoryColors.JEGES
                }

                Tab(
                    text = { Text(text = title, color = colors.textColor) },
                    selected = tabIndex == index,
                    onClick = { tabIndex = index },
                    modifier = Modifier.background(color = colors.backgroundColor)
                )
            }
        }
    }
}
