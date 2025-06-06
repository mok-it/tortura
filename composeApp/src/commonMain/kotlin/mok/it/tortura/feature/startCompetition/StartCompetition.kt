package mok.it.tortura.feature.startCompetition

import NavigateBackIcon
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.dialogs.compose.rememberFileSaverLauncher
import io.github.vinceglb.filekit.name

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
            viewModel.onEvent(StartCompetitionViewModel.StartCompetitionEvent.SaveToFile(file))
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Új verseny indítása") },
                navigationIcon = {
                    NavigateBackIcon()
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
                            StartCompetitionViewModel.StartCompetitionEvent.SelectTeamAssignment(it, file)
                        )
                    }
                }
                val problemSelector = rememberFilePickerLauncher { file ->
                    if (file != null) {
                        viewModel.onEvent(
                            StartCompetitionViewModel.StartCompetitionEvent.SelectProblemSet(it, file)
                        )
                    }
                }
                Row {
                    Text(it.teamAssignmentFile?.name ?: "Kérlek válassz csapatfájlt!")
                    Button(onClick = {
                        teamSelector.launch()
                    }
                    ) {
                        Text("Választás...")
                    }
                    Text(it.problemSetFile?.name ?: "Kérlek válassz feladatsorfájlt")
                    Button(onClick = {
                        problemSelector.launch()
                    }
                    ) {
                        Text("Választás...")
                    }
                }
            }
            item {
                IconButton(
                    onClick = {
                        viewModel.onEvent(
                            StartCompetitionViewModel.StartCompetitionEvent.AddRow
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
                            viewModel.onEvent(StartCompetitionViewModel.StartCompetitionEvent.SaveToDatabase)
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

            StartCompetitionViewModel.StartCompetitionPopupType.NONE -> {/*No popup should be displayed*/
            }
        }
    }
}

@Composable
fun ParseErrorPopup(onDismiss: () -> Unit) {
    GenericErrorPopup(
        "Formátumhiba",
        "A fájl formátuma helytelen, biztosan jót választottál?" +
                "Excel fájlok beolvasása még nem lehetséges, a beosztást az appban hozd létre!",
        onDismiss
    )
}

@Composable
fun TypeErrorPopup(onDismiss: () -> Unit) {
    GenericErrorPopup(
        "Tartalomhiba",
        "A fájl formátuma helyes ,de tartalma nem értelmezhető. Biztos jót választottál?" +
                "Baloldat csapatbeosztást (.csp), jobboldalt feladatsort (.fes) válassz ki!",
        onDismiss
    )
}

@Composable
fun SaveErrorPopup(onDismiss: () -> Unit) {
    GenericErrorPopup(
        "Mentési hiba",
        "A fájl mentése nem sikerült, mert egy vagy több helyen" +
                "nincs csapat vagy feladatsor kiválasztva.",
        onDismiss
    )
}

@Composable
fun GenericErrorPopup(
    title: String,
    text: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        title = { Text(title) },
        text = { Text(text) },
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = onDismiss,
            ) { Text("Rendben") }
        }
    )
}