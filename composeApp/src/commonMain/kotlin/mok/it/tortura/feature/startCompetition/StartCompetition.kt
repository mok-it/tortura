package mok.it.tortura.feature.startCompetition

import NavigateBackIcon
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.dialogs.compose.rememberFileSaverLauncher
import io.github.vinceglb.filekit.name
import mok.it.tortura.feature.startCompetition.StartCompetitionViewModel.StartCompetitionEvent
import mok.it.tortura.ui.CategoryColors
import mok.it.tortura.ui.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartCompetiton(
    onBack: () -> Unit,
    onStart: () -> Unit,
) {
    val viewModel = viewModel { StartCompetitionViewModel() }
    val rows by remember { viewModel.rows }
    val popup by remember { viewModel.popup }
    val save = rememberFileSaverLauncher { file ->
        if (file != null) {
            viewModel.onEvent(StartCompetitionEvent.SaveToFile(file))
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Új verseny indítása") },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                    ) {
                        NavigateBackIcon()
                    }
                },
                actions = {
                    HelpButton(
                        onClick = { viewModel.popup.value = StartCompetitionViewModel.StartCompetitionPopupType.HELP },
                    )
                },
            )
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues)
        ) {
            items(rows) {
                val teamSelector = rememberFilePickerLauncher { file ->
                    if (file != null) {
                        viewModel.onEvent(
                            StartCompetitionEvent.SelectTeamAssignment(it, file)
                        )
                    }
                }
                val problemSelector = rememberFilePickerLauncher { file ->
                    if (file != null) {
                        viewModel.onEvent(
                            StartCompetitionEvent.SelectProblemSet(it, file)
                        )
                    }
                }
                LazyRow {

                    item {
                        Text(it.teamAssignmentFile?.name ?: "Kérlek válassz csapatfájlt!")
                        Button(
                            onClick = {
                                teamSelector.launch()
                            }
                        ) {
                            Text("Választás...")
                        }
                    }
                    item {
                        Text(it.problemSetFile?.name ?: "Kérlek válassz feladatsorfájlt")
                        Button(onClick = {
                            problemSelector.launch()
                        }
                        ) {
                            Text("Választás...")
                        }
                    }

                    item {
                        Text("Kategória:", modifier = Modifier.padding(10.dp))
                        TextField(
                            value = it.category,
                            onValueChange = { string ->
                                viewModel.onEvent(
                                    StartCompetitionEvent.ChangeCompetitionCategory(it, string)
                                )
                            },
                            modifier = Modifier.padding(10.dp),
                        )
                    }

                    item {
                        val colorDropDownExpanded = remember { mutableStateOf(false) }

                        Box(
                            modifier = Modifier.padding(10.dp),
                        ) {
                            TextField(
                                it.colors.name,
                                onValueChange = {},
                                trailingIcon = {
                                    IconButton(onClick = {
                                        colorDropDownExpanded.value = !colorDropDownExpanded.value
                                    }) {
                                        Icon(Icons.Filled.ArrowDropDown, "Select color")
                                    }
                                },
                            )

                            DropdownMenu(
                                expanded = colorDropDownExpanded.value,
                                onDismissRequest = { colorDropDownExpanded.value = false },
                            ) {
                                val colors = listOf(
                                    CategoryColors.BOCS, CategoryColors.KIS, CategoryColors.NAGY,
                                    CategoryColors.JEGES
                                )

                                colors.forEach { color ->
                                    DropdownMenuItem(
                                        text = { Text(color.name) },
                                        onClick = {
                                            viewModel.onEvent(
                                                StartCompetitionEvent.ChangeCompetitionColors(it, color)
                                            )
                                            colorDropDownExpanded.value = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
            item {
                IconButton(
                    onClick = {
                        viewModel.onEvent(
                            StartCompetitionEvent.AddRow
                        )
                    }
                ) {
                    Icon(Icons.Filled.Add, "Add another competition")
                }
            }

            item {
                Row {

                    Button(
                        enabled = viewModel.canSave,
                        onClick = {
                            save.launch("Tortura", "ttr")
                        }
                    ) {
                        Text("Mentés")
                    }
                    Button(
                        enabled = viewModel.canSave,
                        onClick = {
                            viewModel.onEvent(StartCompetitionEvent.SaveToDatabase)
                            onStart()
                        }
                    ) {
                        Text("Indítás")
                    }
                }
            }
        }

        when (popup) {
            StartCompetitionViewModel.StartCompetitionPopupType.PARSE_ERROR -> ParseErrorPopup {
                viewModel.popup.value = StartCompetitionViewModel.StartCompetitionPopupType.NONE
            }

            StartCompetitionViewModel.StartCompetitionPopupType.TYPE_ERROR -> TypeErrorPopup {
                viewModel.popup.value = StartCompetitionViewModel.StartCompetitionPopupType.NONE
            }

            StartCompetitionViewModel.StartCompetitionPopupType.SAVE_ERROR -> SaveErrorPopup {
                viewModel.popup.value = StartCompetitionViewModel.StartCompetitionPopupType.NONE
            }

            StartCompetitionViewModel.StartCompetitionPopupType.HELP -> HelpDialog {
                viewModel.popup.value = StartCompetitionViewModel.StartCompetitionPopupType.NONE
            }

            StartCompetitionViewModel.StartCompetitionPopupType.NONE -> {/*No popup should be displayed*/
            }
        }
    }
}
