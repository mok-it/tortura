package mok.it.tortura.feature.ongoingCompetition

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material3.*
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
import io.github.vinceglb.filekit.dialogs.compose.rememberFileSaverLauncher
import mok.it.tortura.model.Competition
import mok.it.tortura.ui.components.AnswerBlock

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OngoingCompetition(
    onBack: () -> Unit,
) {

    val viewModel = viewModel { OngoingCompetitionViewModel() }

    val competitions by remember { viewModel.competitions }

    val save = rememberFileSaverLauncher { file ->
        if (file != null) {
            viewModel.onEvent(
                OnGoingCompetitionEvent.SaveToFile(file)
            )
        }
    }

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
                    Button(
                        onClick = { save.launch(suggestedName = "tortura", extension = "ttr") }
                    ){
                        Text("Export")
                    }

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
                val competitionTeams = competition.answers
                competitionTeams.forEachIndexed { index, competitionTeam ->
                    if (tabIndex == teamsInPreviousCompetitions(competition) + index) {
                        if (competitionTeam.answer.finished) {
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
                                    text = "Pontok: ${competitionTeam.answer.points()}",
                                    fontSize = 40.sp,
                                    color = competition.teamAssignment.colorSchema.textColor,
                                    modifier = Modifier.background(
                                        color = competition.teamAssignment.colorSchema.backgroundColor,
                                        shape = RoundedCornerShape(4.dp)
                                    )
                                )

                                Text(
                                    text = "Kezdés: ${competition.startTime}\nVége: ${competitionTeam.answer.lastAnswerTime}"
                                )
                            }


                        } else {

                            val showConfirmDialog = remember { mutableStateOf(false) }

                            AnswerBlock(
                                teamName = "${(competitions.indexOf(competition) + 1) * 100 + index}",
                                answers = competitionTeam.answer.currentBlockAnswer,
                                indexOffset = competition.problemSet.previousTaskNumber( competitionTeam.answer.currentBlockAnswer.block ),
                                modifyAnswer = { task, newAnswer ->
                                    viewModel.onEvent(
                                        OnGoingCompetitionEvent.ModifyAnswer(
                                            competition,
                                            competitionTeam,
                                            task,
                                            newAnswer
                                        )
                                    )
                                },
                                onRestartBlock = {
                                    viewModel.onEvent(
                                        OnGoingCompetitionEvent.RestartBlock(
                                            competition,
                                            competitionTeam
                                        )
                                    )
                                },
                                onNextBlock = {
                                    viewModel.onEvent(
                                        OnGoingCompetitionEvent.NextBlock(
                                            competition,
                                            competitionTeam
                                        )
                                    )
                                },
                                onNavigateForwards = {
                                    viewModel.onEvent(
                                        OnGoingCompetitionEvent.NavigateForwards(
                                            competition,
                                            competitionTeam
                                        )
                                    )
                                },
                                onNavigateBackWards = {
                                    viewModel.onEvent(
                                        OnGoingCompetitionEvent.NavigateBackwards(
                                            competition,
                                            competitionTeam
                                        )
                                    )
                                },
                                onDeleteLastTry = {
                                    showConfirmDialog.value = true
                                },
                                navigateBackWardsEnabled = competitionTeam.answer.canNavigateBackward,
                                navigateForwardsEnabled = competitionTeam.answer.canNavigateForward,
                                deleteLastTryEnabled = competitionTeam.answer.canDeleteLastTry,
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
                                                    OnGoingCompetitionEvent.DeleteLastTry(competition, competitionTeam)
                                                )
                                                showConfirmDialog.value = false
                                            },
                                            colors = ButtonDefaults.buttonColors( containerColor = Color.Red )
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
                containerColor = Color.Transparent,
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
