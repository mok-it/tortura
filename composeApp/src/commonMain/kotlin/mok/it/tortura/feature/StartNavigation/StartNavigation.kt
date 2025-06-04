package mok.it.tortura.feature.StartNavigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher

@Composable
fun StartNavigation(
    newFromFile: (() -> Unit),
    openCompetition: (() -> Unit),
    onBack: (() -> Unit),
) {
    val viewModel = viewModel { StartNavigationViewModel() }
    val dialogState by remember { viewModel.dialogState }
    val importSelector = rememberFilePickerLauncher { file ->
        if (file != null) {
            viewModel.onEvent(
                StartNavigationViewModel.StartNavigationEvent.LoadFromFile(
                    file,
                    openCompetition
                )
            )
        }
    }
    Scaffold(
        topBar = {

            TopAppBar(
                title = { Text("Verseny indítása") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
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
            Button(onClick = openCompetition) {
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

                        importSelector.launch()

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