package ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import model.Competition
import ui.components.AnswerBlock
import viewmodel.OngoingCompetitionViewModel

@Composable
fun OngoingCompetition(
    onBack: () -> Unit,
) {

    val viewModel = OngoingCompetitionViewModel()

    fun teamsInPreviousCompetitions(actCompetition: Competition): Int{
        var counter = 0
        for( competition in viewModel.competitions ){
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
            for( competition in viewModel.competitions ) {
                val teams = competition.teamAssignment.teams
                teams.forEachIndexed { index, team ->
                    if( tabIndex == teamsInPreviousCompetitions(competition) + index ) {
                        AnswerBlock(
                            block = competition.getTeamCurrentBlock(team),
                            teamName = "${ (viewModel.competitions.indexOf(competition) + 1) * 100 + index }",
                            indexOffset = 0,
                            modifyAnswer = { _,_ ->  },
                            onCheckSolutions = {},
                            textColor = competition.teamAssignment.colorSchema.textColor,
                            backgroundColor = competition.teamAssignment.colorSchema.backgroundColor,
                        )
                    }
                }
            }
            ScrollableTabRow(
                selectedTabIndex = tabIndex,
                backgroundColor = Color.Transparent,
            ) {
                for( competition in viewModel.competitions ){
                    val teams = competition.teamAssignment.teams
                    teams.forEachIndexed{ index, _->
                        Tab(
                            selected = tabIndex == teamsInPreviousCompetitions(competition) + index,
                            onClick = { tabIndex = teamsInPreviousCompetitions(competition) + index },
                            text= {
                                Text(
                                    text = "${ (viewModel.competitions.indexOf(competition) + 1) * 100 + index }",
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
