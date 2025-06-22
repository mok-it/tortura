package mok.it.tortura.feature.continueCompetition

import AddIcon
import DeleteIcon
import NavigateBackIcon
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.dialogs.compose.rememberFileSaverLauncher
import io.github.vinceglb.filekit.name
import mok.it.tortura.ui.components.HelpButton
import mok.it.tortura.ui.components.HelpDialog
import mok.it.tortura.ui.components.ParseErrorPopup
import mok.it.tortura.ui.components.TypeErrorPopup

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContinueCompetition(
    onBack: () -> Unit,
    onStart: () -> Unit,
) {
    val viewmodel = viewModel{ ContinueCompetitionViewmodel() }
    val rows by remember { viewmodel.rows }
    val popup by remember { viewmodel.popup }

    when (popup) {
        ContinueCompetitionPopupType.PARSE_ERROR -> {
            ParseErrorPopup { viewmodel.onEvent(ContinueCompetitionEvent.DismissPopup) }
        }
        ContinueCompetitionPopupType.TYPE_ERROR -> {
            TypeErrorPopup { viewmodel.onEvent(ContinueCompetitionEvent.DismissPopup) }
        }

        ContinueCompetitionPopupType.HELP -> {
            HelpDialog { viewmodel.onEvent(ContinueCompetitionEvent.DismissPopup) }
        }

        ContinueCompetitionPopupType.NONE -> { /* No popup should be shown */ }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Tortúrák folytatása") },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                    ) {
                        NavigateBackIcon()
                    }
                },
                actions = {
                    HelpButton(
                        onClick = { viewmodel.onEvent(ContinueCompetitionEvent.ShowHelp) },
                    )
                },
            )
        }
    ){ paddingValues ->
        LazyColumn( modifier = Modifier.padding(paddingValues) ) {
            for( row in rows ){


                item{
                    val fileSelector = rememberFilePickerLauncher { file ->
                        if (file != null) {
                            viewmodel.onEvent(ContinueCompetitionEvent.ChangeFile(row, file))
                        }
                    }

                    Row {
                        Text( text = row.file?.name ?: "Válassz tortúrafájlt" )

                        Button(
                            onClick = { fileSelector.launch() },
                        ) {
                            Text(text = "Fájl kiválasztása")
                        }

                        IconButton(
                            onClick = { viewmodel.onEvent(ContinueCompetitionEvent.DeleteRow( row ) ) }
                        ){
                            DeleteIcon()
                        }
                    }
                }
            }

            item{
                IconButton(
                    onClick = { viewmodel.onEvent(ContinueCompetitionEvent.AddRow) }
                ) {
                    AddIcon()
                }
            }

            item{

                val saveLauncher = rememberFileSaverLauncher { file ->
                    if (file != null) {
                        viewmodel.onEvent(ContinueCompetitionEvent.SaveCompetitionsToFile(file))
                    }
                }

                Row {
                    Button(
                        onClick = { saveLauncher.launch( "Tortura", "ttr" ) }
                    ) {
                        Text("Mentés")
                    }

                    Button(
                        onClick = {
                            viewmodel.onEvent(ContinueCompetitionEvent.SaveCompetitionsToDatabase)
                            onStart()
                        }
                    ){
                        Text( "Indítás" )
                    }
                }
            }
        }
    }
}