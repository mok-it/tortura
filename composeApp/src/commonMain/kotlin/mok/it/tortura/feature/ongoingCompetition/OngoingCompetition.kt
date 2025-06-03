package mok.it.tortura.feature.ongoingCompetition

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import mok.it.tortura.model.Competition
import mok.it.tortura.ui.components.AnswerBlock

@Composable
fun OngoingCompetition(
    onBack: () -> Unit,
) {

    val viewModel = viewModel { OngoingCompetitionViewModel() }

    val competitions by remember { viewModel.competitions }

    fun teamsInPreviousCompetitions(actCompetition: Competition): Int {
        var counter = 0
        for (competition in competitions) {
            if (competition == actCompetition) return counter
            counter += competition.teamAssignment.teams.size
        }
        return counter
    }

    val tabIndex by remember { viewModel.tabIndex }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Verseny") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
//                    CustomizableSearchBar(
//                        query = searchText,
//                        onQueryChange = { viewModel.onEvent(OnGoingCompetitionEvent.SearchTextChange( it ) ) },
//                        onSearch = {  },
//                        searchResults = viewModel.getAllStudentsList(),
//                        onResultClick = { viewModel.onEvent(OnGoingCompetitionEvent.SearchForStudent(it)) },
//                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(innerPadding),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            for (competition in competitions) {
                val teams = competition.teamAssignment.teams
                teams.forEachIndexed { index, team ->
                    if (tabIndex == teamsInPreviousCompetitions(competition) + index) {
                        if (competition.answers[team]!!.finished) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.SpaceEvenly,
                                modifier = Modifier.fillMaxHeight().weight(1f)
                            ) {
                                Text(
                                    text = "Végeztek",
                                    fontSize = 50.sp,
                                    color = competition.teamAssignment.colorSchema.textColor,
                                    modifier = Modifier.background(
                                        color = competition.teamAssignment.colorSchema.backgroundColor,
                                        shape = RoundedCornerShape(4.dp)
                                    )
                                )

                                Text(
                                    text = "Pontok: ${competition.answers[team]!!.points()}",
                                    fontSize = 40.sp,
                                    color = competition.teamAssignment.colorSchema.textColor,
                                    modifier = Modifier.background(
                                        color = competition.teamAssignment.colorSchema.backgroundColor,
                                        shape = RoundedCornerShape(4.dp)
                                    )
                                )
                            }


                        } else {

                            val showConfirmDialog = remember { mutableStateOf(false) }

                            AnswerBlock(
                                teamName = "${(competitions.indexOf(competition) + 1) * 100 + index}",
                                answers = competition.answers[team]!!.currentBlockAnswer,
                                indexOffset = competition.answers[team]!!.previousTaskNumber,
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
                                onNavigateForwards = {
                                    viewModel.onEvent(
                                        OnGoingCompetitionEvent.NavigateForwards(
                                            competition,
                                            team
                                        )
                                    )
                                },
                                onNavigateBackWards = {
                                    viewModel.onEvent(
                                        OnGoingCompetitionEvent.NavigateBackwards(
                                            competition,
                                            team
                                        )
                                    )
                                },
                                onDeleteLastTry = {
                                    showConfirmDialog.value = true
                                },
                                navigateBackWardsEnabled = competition.answers[team]!!.canNavigateBackward,
                                navigateForwardsEnabled = competition.answers[team]!!.canNavigateForward,
                                deleteLastTryEnabled = competition.answers[team]!!.canDeleteLastTry,
                                textColor = competition.teamAssignment.colorSchema.textColor,
                                backgroundColor = competition.teamAssignment.colorSchema.backgroundColor,
                                modifier = Modifier.weight(1f)
                            )

                            if (showConfirmDialog.value) {
                                AlertDialog(
                                    title = { Text("Próbálkozás törlése") },
                                    text = { Text("Egész biztos törölni akarod?\nUtána már nem fogom visszaimádkozni sehogy...") },
                                    onDismissRequest = {
                                        showConfirmDialog.value = false
                                    },
                                    confirmButton = {
                                        Button(
                                            onClick = {
                                                viewModel.onEvent(
                                                    OnGoingCompetitionEvent.DeleteLastTry(competition, team)
                                                )
                                                showConfirmDialog.value = false
                                            },
                                            colors = ButtonDefaults.buttonColors( backgroundColor = Color.Red )
                                        ) {
                                            Row {
                                                Icon(Icons.Filled.DeleteForever, null)
                                                Text("Biztos")
                                            }
                                        }
                                    },
                                    dismissButton = {
                                        Button(onClick = {
                                            showConfirmDialog.value = false
                                        }) {
                                            Text("Hupsz, mégse")
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }

            ScrollableTabRow(
                selectedTabIndex = tabIndex,
                backgroundColor = Color.Transparent,
                modifier = Modifier
            ) {
                for (competition in competitions) {
                    val teams = competition.teamAssignment.teams
                    teams.forEachIndexed { index, _ ->
                        Tab(
                            selected = tabIndex == teamsInPreviousCompetitions(competition) + index,
                            onClick = {
                                viewModel.onEvent(
                                    OnGoingCompetitionEvent.ChangeTabIndex(teamsInPreviousCompetitions(competition) + index)
                                )
                            },
                            text = {
                                Text(
                                    text = "${(competitions.indexOf(competition) + 1) * 100 + index}",
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
