package mok.it.tortura.feature.evaluation

import NavigateBackIcon
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.vinceglb.filekit.dialogs.compose.rememberFileSaverLauncher
import kotlinx.coroutines.launch
import mok.it.tortura.saveStringToFile

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Evaluation(
    onBack: () -> Unit
) {
    val viewModel = viewModel { EvaluationViewModel() }
    val competitions by remember { viewModel.competitions }
    val tabIndex by remember { viewModel.tabIndex }
    var exportMenuExpanded by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val htmlSaveLauncher = rememberFileSaverLauncher { file ->
        if (file != null) {
            scope.launch {
                saveStringToFile(file, viewModel.getPrintableHtml())
            }
        }
    }
    val csvSaveLauncher = rememberFileSaverLauncher { file ->
        if (file != null) {
            scope.launch {
                saveStringToFile(file, viewModel.getCsv())
            }
        }
    }
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
                            Icon(Icons.Default.Download, contentDescription = "More options")
                        }
                        DropdownMenu(
                            expanded = exportMenuExpanded,
                            onDismissRequest = { exportMenuExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Exportálás nyomtatható HTML-be") },
                                onClick = {
                                    htmlSaveLauncher.launch(
                                        "${competitions[tabIndex].category}-eredmenyek",
                                        "html"
                                    )
                                    exportMenuExpanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Exportálás CSV-be") },
                                onClick = {
                                    csvSaveLauncher.launch(
                                        "${competitions[tabIndex].category}-eredmenyek",
                                        "csv"
                                    )
                                    exportMenuExpanded = false
                                }
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
                                text = competition.category,
                                color = competition.colors.textColor
                            )
                        },
                        modifier = Modifier.background(competition.colors.backgroundColor)
                    )
                }
            }
        }
    ) { innerPadding ->

        val competition = competitions[tabIndex]
        val maxTeamSize = competition.teamAssignment.teams.maxOfOrNull { it.students.size } ?: 4
        LazyVerticalGrid(
            GridCells.Fixed(maxTeamSize + 2),
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
                item {
                    Text(
                        (competition.teamAssignment.teams.indexOf(team) +
                                ((competitions.indexOf(competition) + 1) * 100)).toString()
                    )
                }
                for (i in 0..<maxTeamSize) {
                    val student = team.students.getOrNull(i)
                    item {
                        if (student?.group == "")
                            Text((student.name))
                        else if (student != null) //student is not null, and group string is provided
                            Text("${student.name} (${student.group})")
                        else {
                            Text("--")
                        }
                    }

                }
                item { Text(answer.answer.points().toString()) }
            }
        }

    }
}