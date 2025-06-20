package mok.it.tortura.feature.createProblemSet

import NavigateBackIcon
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.dialogs.compose.rememberFileSaverLauncher
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import mok.it.tortura.getPlatform
import mok.it.tortura.saveStringToFile
import mok.it.tortura.ui.components.*

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CreateProblemSet(
    onBack: () -> Unit,
) {
    val viewModel: CreateProblemSetViewModel = viewModel { CreateProblemSetViewModel() }
    val problemSet by remember { viewModel.problemSet }
    val popup by remember { viewModel.popup }

    val scope = rememberCoroutineScope()
    val launcher = rememberFileSaverLauncher { file ->
        if (file != null) {
            scope.launch {
                saveStringToFile(file, Json.encodeToString(problemSet))
            }
        }
    }

    val loadFromJsonLauncher = rememberFilePickerLauncher { file ->
        if (file != null) {
            viewModel.onEvent(CreateProblemSetEvent.ImportProblemSetFromJson(file) )
        }
    }

    val loadFromExcelLauncher = rememberFilePickerLauncher { file ->
        if (file != null) {
            viewModel.onEvent(CreateProblemSetEvent.ImportProblemSetFromExcel(file))
        }
    }

    when( popup ){
        CreateProblemSetPopupType.PARSE_ERROR -> {
            ParseErrorPopup { viewModel.onEvent(CreateProblemSetEvent.DismissPopup ) }
        }
        CreateProblemSetPopupType.TYPE_ERROR -> {
            TypeErrorPopup { viewModel.onEvent(CreateProblemSetEvent.DismissPopup ) }
        }
        CreateProblemSetPopupType.EXCEL_ERROR -> {
            ExcelErrorPopup { viewModel.onEvent(CreateProblemSetEvent.DismissPopup ) }
        }
        CreateProblemSetPopupType.HELP -> {
            HelpDialog { viewModel.onEvent(CreateProblemSetEvent.DismissPopup ) }
        }
        CreateProblemSetPopupType.NONE -> { /* No popup should be shown */ }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text("Create problem Set")},
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        NavigateBackIcon()
                    }
                },
                actions = {
                    Button(
                        onClick = {
                            loadFromExcelLauncher.launch()
                        },
                        enabled = getPlatform().excelImportImplemented,
                    ){
                        Text("Import from Excel")
                    }
                    Button(
                        onClick = {
                            loadFromJsonLauncher.launch()
                        }
                    ){
                        Text("Import")
                    }

                    HelpButton(
                        onClick = { viewModel.onEvent(CreateProblemSetEvent.ShowHelp ) },
                    )
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            LazyColumn(modifier = Modifier.weight(1f)) {
                for (block in problemSet.blocks) {
                    stickyHeader {
                        Surface(color = Color.Cyan, modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    "${problemSet.blocks.indexOf(block) + 1}. blokk",
                                    modifier = Modifier.padding(end = 8.dp)
                                )
                                IconButton(onClick = {
                                    viewModel.onEvent(CreateProblemSetEvent.DeleteBlock(block))
                                }, modifier = Modifier.size(40.dp)) {
                                    Icon(Icons.Filled.Delete, "Blokk törlése")
                                }
                            }

                        }
                    }
                    items(block.tasks) {
                        TaskCard(
                            task = it,
                            onChangeText = { text ->
                                viewModel.onEvent(
                                    CreateProblemSetEvent.ChangeTaskText(
                                        block,
                                        it,
                                        text
                                    )
                                )
                            },
                            onChangeSolution = { text ->
                                viewModel.onEvent(
                                    CreateProblemSetEvent.ChangeTaskSolution(
                                        block,
                                        it,
                                        text
                                    )
                                )
                            },
                            onDeleteTask = { viewModel.onEvent(CreateProblemSetEvent.DeleteTask(it, block)) }
                        )
                    }
                    item {
                        Button(
                            onClick = {
                                viewModel.onEvent(CreateProblemSetEvent.AddTask(block))
                            },
                        ) {
                            Row {
                                Icon(Icons.Default.Menu, "")
                                Text("Feladat hozzáadása")
                            }
                        }
                    }
                }
                item {
                    Button(shape = CircleShape, onClick = {
                        viewModel.onEvent(CreateProblemSetEvent.AddBlock)
                    }) {
                        Row {
                            Icon(Icons.Default.Add, "", modifier = Modifier.size(50.dp))
                            Text("Blokk hozzáadása")
                        }
                    }
                }
            }
            Button(
                onClick = { launcher.launch("output", "txt") }
            ) { Text(text = "Mentés") }
        }
    }
}
