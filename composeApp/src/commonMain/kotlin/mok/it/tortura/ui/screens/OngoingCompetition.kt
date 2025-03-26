package ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import mok.it.tortura.model.Competition
import ui.components.AnswerBlock
import viewmodel.OnGoingCompetitionEvent
import viewmodel.OngoingCompetitionViewModel

@Composable
fun OngoingCompetition(
    onBack: () -> Unit,
) {

    val viewModel = viewModel { OngoingCompetitionViewModel() }

    val competitions by remember { viewModel.competitions }

    fun teamsInPreviousCompetitions(actCompetition: Competition): Int{
        var counter = 0
        for( competition in competitions ) {
            if( competition == actCompetition ) return counter
            counter += competition.teamAssignment.teams.size
        }
        return counter
    }

    var tabIndex by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Verseny") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back") } }
            )
        }
    ){ innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(innerPadding),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            for( competition in competitions ) {
                val teams = competition.teamAssignment.teams
                teams.forEachIndexed { index, team ->
                    if( tabIndex == teamsInPreviousCompetitions(competition) + index ) {
                        if( competition.answers[team]!!.finished ){
                            Text(
                                text = "Végeztek",
                                fontSize = 50.sp,
                                color = competition.teamAssignment.colorSchema.textColor,
                                modifier = Modifier.background( color = competition.teamAssignment.colorSchema.backgroundColor, shape = RoundedCornerShape(4.dp) )
                                )
                        } else {

                            AnswerBlock(
                                teamName = "${(competitions.indexOf(competition) + 1) * 100 + index}",
                                answers = competition.answers[team]!!.answerHistory.last(),
                                indexOffset = 0,
                                modifyAnswer = { task, newAnswer ->
                                    viewModel.onEvent(
                                        OnGoingCompetitionEvent.ModifyAnswer(
                                            competition,
                                            team,
                                            task,
                                            newAnswer
                                        )
                                    )
                                },
                                onRestartBlock = {
                                    viewModel.onEvent(
                                        OnGoingCompetitionEvent.RestartBlock(
                                            competition,
                                            team
                                        )
                                    )
                                },
                                onNextBlock = {
                                    viewModel.onEvent(
                                        OnGoingCompetitionEvent.NextBlock(
                                            competition,
                                            team
                                        )
                                    )
                                },
                                textColor = competition.teamAssignment.colorSchema.textColor,
                                backgroundColor = competition.teamAssignment.colorSchema.backgroundColor,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
            ScrollableTabRow(
                selectedTabIndex = tabIndex,
                backgroundColor = Color.Transparent,
                modifier = Modifier
            ) {
                for( competition in competitions ){
                    val teams = competition.teamAssignment.teams
                    teams.forEachIndexed{ index, _->
                        Tab(
                            selected = tabIndex == teamsInPreviousCompetitions(competition) + index,
                            onClick = { tabIndex = teamsInPreviousCompetitions(competition) + index },
                            text= {
                                Text(
                                    text = "${ (competitions.indexOf(competition) + 1) * 100 + index }",
                                    color = competition.teamAssignment.colorSchema.textColor,
                                )
                            },
                            modifier = Modifier.background(competition.teamAssignment.colorSchema.backgroundColor),
                        )
                    }
                }
            }
        }
    }
}
