package mok.it.tortura.feature.evaluation

import NavigateBackIcon
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Evaluation(
    onBack: () -> Unit
) {
    val viewModel = viewModel { EvaluationViewModel() }
    val competitions by remember { viewModel.competitions }
    val tabIndex by remember { viewModel.tabIndex }
    var exportMenuExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Kiértékelés") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        NavigateBackIcon()
                    }
                },
                actions = {

                    Box(
                        modifier = Modifier
                            .padding(16.dp)
                    ) {
                        IconButton(onClick = { exportMenuExpanded = !exportMenuExpanded }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "More options")
                        }
                        DropdownMenu(
                            expanded = exportMenuExpanded,
                            onDismissRequest = { exportMenuExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Exportálás nyomtatnató PDF-be") },
                                onClick = { /* Do something... */ }
                            )
                            DropdownMenuItem(
                                text = { Text("Option 2") },
                                onClick = { /* Do something... */ }
                            )
                        }
                    }

                }
            )
        },
        bottomBar = {
            ScrollableTabRow(
                selectedTabIndex = tabIndex,
                containerColor = Color.Transparent,
                modifier = Modifier
            ) {
                competitions.forEachIndexed { index, competition ->
                    Tab(
                        selected = tabIndex == index,
                        onClick = {
                            viewModel.onEvent(
                                EvaluationEvent.SelectTabIndex(index)
                            )
                        },
                        text = {
                            Text(
                                text = "${(competitions.indexOf(competition) + 1)}xx", //TODO: szebben is ki lehetne írni
                                color = competition.teamAssignment.colorSchema.textColor
                            )
                        },
                        modifier = Modifier.background(competition.teamAssignment.colorSchema.backgroundColor)
                    )
                }
            }
        }
    ) { innerPadding ->

        val competition = competitions[tabIndex]
        val maxTeamSize = competition.teamAssignment.teams.maxOfOrNull { it.students.size } ?: 4
        LazyVerticalGrid(
            GridCells.Fixed(6),
            contentPadding = PaddingValues(10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize().padding(innerPadding)
        ) {
            item { Text("Azonosító") }
            for (i in 1..maxTeamSize) {
                item { Text("$i. csapattag") }
            }
            item { Text("Pontszám") }
            competition.answers.forEach { answer ->
                val team = answer.team
                item { Text(competition.teamAssignment.teams.indexOf(team).toString()) }
                for (i in 0..<maxTeamSize) {
                    val student = team.students.getOrNull(i)
                    item {
                        if (student?.group == "")
                            Text((student.name))
                        else if (student != null) //student is not null, and group string is provided
                            Text("${student.name} (${student.group})")
                    }

                }
                item { Text(answer.answer.points().toString()) }
            }
        }

    }
}