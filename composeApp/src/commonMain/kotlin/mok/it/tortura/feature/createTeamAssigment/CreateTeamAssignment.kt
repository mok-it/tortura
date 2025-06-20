package mok.it.tortura.feature.createTeamAssigment

import NavigateBackIcon
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.dialogs.compose.rememberFileSaverLauncher
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import mok.it.tortura.saveStringToFile
import mok.it.tortura.ui.components.HelpButton
import mok.it.tortura.ui.components.HelpDialog
import mok.it.tortura.ui.components.StudentCard

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CreateTeamAssignment(
    onBack: () -> Unit,
) {
    val viewModel = viewModel { CreateTeamAssignmentViewModel() }
    val teamAssignment by remember { viewModel.teamAssignment }

    val scope = rememberCoroutineScope()
    val saveLauncher = rememberFileSaverLauncher { file ->
        if (file != null) {
            scope.launch {
                saveStringToFile(file, Json.encodeToString(teamAssignment))
            }
        }
    }
    val lazyListState = rememberLazyListState()

    val loadFromJsonLauncher = rememberFilePickerLauncher { file ->
        if (file != null) {
            viewModel.onEvent( CreateTeamAssignmentEvent.LoadFromJson(file) )
        }
    }

    val helpDialogShown = remember { mutableStateOf(false) }

    if( helpDialogShown.value ) {
        HelpDialog(
            onDismiss = { helpDialogShown.value = false },
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create Team Assignment") },
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        NavigateBackIcon()
                    }
                },
                actions = {
                    Button(
                        onClick = {
                            loadFromJsonLauncher.launch()
                        }
                    ){
                        Text("Import")
                    }

                    HelpButton(
                        onClick = { helpDialogShown.value = true },
                    )
                }
            )
        }
    ) { paddingValues ->
        Row(
            modifier = Modifier.padding(paddingValues)
        ) {
            Surface {
                Column {

                    LazyColumn(
                        state = lazyListState,
                        modifier = Modifier.weight(1f)
                    ) {
                        for (team in teamAssignment.teams) {
                            stickyHeader {
                                Surface(color = Color.Cyan, modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            "${teamAssignment.teams.indexOf(team) + 1}. csapat",
                                            modifier = Modifier.padding(end = 8.dp)
                                        )
                                        IconButton(onClick = {
                                            viewModel.onEvent(CreateTeamAssignmentEvent.DeleteTeam(team))
                                        }, modifier = Modifier.size(40.dp)) {
                                            Icon(Icons.Filled.Delete, "Csapat törlése")
                                        }
                                    }
                                }
                            }

                            items(team.students) { student ->
                                StudentCard(
                                    student,
                                    onChangeName = { newName ->
                                        viewModel.onEvent(CreateTeamAssignmentEvent.ChangeStudentName(team, student, newName))
                                    },
                                    onChangeGroup = { newGroup ->
                                        viewModel.onEvent(CreateTeamAssignmentEvent.ChangeStudentGroup(team, student, newGroup))
                                    },
                                    onChangeKlass = { newKlass ->
                                        viewModel.onEvent(CreateTeamAssignmentEvent.ChangeStudentKlass(team, student, newKlass))
                                    },
                                    onDeleteStudent = {
                                        viewModel.onEvent(CreateTeamAssignmentEvent.DeleteMember(team, student))
                                    }
                                )
                            }

                            item {
                                Button(
                                    onClick = {
                                        viewModel.onEvent(CreateTeamAssignmentEvent.AddStudent(team))
                                    },
                                ) {
                                    Row {
                                        Icon(Icons.Default.Person, "")
                                        Text("Csapattag hozzáadása")
                                    }
                                }
                            }

                        }
                        item {
                            Button(shape = CircleShape, onClick = {
                                viewModel.onEvent(CreateTeamAssignmentEvent.AddTeam)
                            }) {
                                Row {
                                    Icon(Icons.Default.Add, "", modifier = Modifier.size(50.dp))
                                    Text("Csapat hozzáadása")
                                }
                            }
                        }
                    }
                    Button(
                        onClick = { saveLauncher.launch("output", "txt") }
                    ) { Text(text = "Mentés") }
                }
            }
        }
    }
}