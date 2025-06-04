package mok.it.tortura.feature.createProblemSet

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.vinceglb.filekit.dialogs.compose.rememberFileSaverLauncher
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import mok.it.tortura.saveStringToFile
import mok.it.tortura.ui.components.TaskCard

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CreateProblemSet(
    onBack: () -> Unit,
) {
    val viewModel: CreateProblemSetViewModel = viewModel { CreateProblemSetViewModel() }
    val problemSet by remember { viewModel.problemSet }

    val scope = rememberCoroutineScope()
    val launcher = rememberFileSaverLauncher { file ->
        if (file != null) {
            scope.launch {
                saveStringToFile(file, Json.encodeToString(problemSet))
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text("Create problem Set")},
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) {

        Column {
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
                                    viewModel.onEvent(CompetitionEditEvent.DeleteBlock(block))
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
                                    CompetitionEditEvent.ChangeTaskText(
                                        block,
                                        it,
                                        text
                                    )
                                )
                            },
                            onChangeSolution = { text ->
                                viewModel.onEvent(
                                    CompetitionEditEvent.ChangeTaskSolution(
                                        block,
                                        it,
                                        text
                                    )
                                )
                            },
                            onDeleteTask = { viewModel.onEvent(CompetitionEditEvent.DeleteTask(it, block)) }
                        )
                    }
                    item {
                        Button(
                            onClick = {
                                viewModel.onEvent(CompetitionEditEvent.AddTask(block))
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
                        viewModel.onEvent(CompetitionEditEvent.AddBlock)
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
