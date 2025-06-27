package mok.it.tortura.feature.StartNavigation

import NavigateBackIcon
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import mok.it.tortura.CompetitionDb

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartNavigation(
    newFromFile: () -> Unit,
    openCompetition: () -> Unit,
    onImport: () -> Unit,
    onBack: () -> Unit,
) {
    val viewModel = viewModel { StartNavigationViewModel() }
    val dialogState by remember { viewModel.dialogState }
    Scaffold(
        topBar = {

            TopAppBar(
                title = { Text("Verseny indítása") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        NavigateBackIcon()
                    }
                },
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(onClick = {
                viewModel.onEvent(StartNavigationViewModel.StartNavigationEvent.NewFromFile)
            }) {
                Text(text = "Új verseny fájlból")
            }
            Button(
                onClick = openCompetition,
                enabled = !CompetitionDb.isEmpty
            ) {
                Text(text = "Helyi verseny folytatása")
            }
            Button(onClick = {
                viewModel.onEvent(StartNavigationViewModel.StartNavigationEvent.ContinueFromFile)
            }) {
                Text(text = "Fájlból importálás és folytatás")
            }

        }
        when (dialogState) {
            StartNavigationViewModel.StartNavigationState.NEW_FROM_FILE -> {
                discardLocalWarning(
                    onDismiss = { viewModel.onEvent(StartNavigationViewModel.StartNavigationEvent.DismissDialog) },
                    onDiscard = {
                        viewModel.onEvent(StartNavigationViewModel.StartNavigationEvent.DismissDialog)
                        newFromFile()
                    }
                )
            }

            StartNavigationViewModel.StartNavigationState.CONTINUE_FROM_FILE -> {
                discardLocalWarning(
                    onDismiss = {
                        viewModel.onEvent(StartNavigationViewModel.StartNavigationEvent.DismissDialog)
                    },
                    onDiscard = {
                        viewModel.onEvent(StartNavigationViewModel.StartNavigationEvent.DismissDialog)
                        onImport()
                    }
                )
            }

            StartNavigationViewModel.StartNavigationState.NONE -> {/*No dialog shown*/
            }
        }
    }
}


@Composable
fun discardLocalWarning(
    onDismiss: () -> Unit,
    onDiscard: () -> Unit,
) {
    AlertDialog(
        title = { Text("Helyi verseny eldobása") },
        text = {
            Text(
                "Jelenleg már folyamatban van egy helyi verseny, amit a folytatáshoz törölnöd kell." +
                        "Biztosan törölni szeretnéd?"
            )
        },
        onDismissRequest = { onDismiss },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Mégse")
            }
        },
        confirmButton = {
            Button(onClick = onDiscard) {
                Text("Törlés")
            }
        },
    )
}