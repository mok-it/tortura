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
import androidx.compose.material.icons.filled.MoreVert
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
import mok.it.tortura.ui.components.*

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CreateTeamAssignment(
    onBack: () -> Unit,
) {
    val viewModel = viewModel { CreateTeamAssignmentViewModel() }
    val teamAssignment by remember { viewModel.teamAssignment }
    val popup by remember { viewModel.popup }
    val menuExpanded = remember { mutableStateOf(false) }

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
            viewModel.onEvent(CreateTeamAssignmentEvent.LoadFromJson(file))
        }
    }

    val loadFromCsvLauncher = rememberFilePickerLauncher { file ->
        if (file != null) {
            viewModel.onEvent(CreateTeamAssignmentEvent.LoadFromCsv(file))
        }
    }

    val loadFromExcelLauncher = rememberFilePickerLauncher { file ->
        if (file != null) {
            viewModel.onEvent(CreateTeamAssignmentEvent.LoadFromExcel(file))
        }
    }

    when (popup) {
        CreateTeamAssignmentPopupType.PARSE_ERROR -> {
            ParseErrorPopup { viewModel.onEvent(CreateTeamAssignmentEvent.DismissPopup) }
        }

        CreateTeamAssignmentPopupType.TYPE_ERROR -> {
            TypeErrorPopup { viewModel.onEvent(CreateTeamAssignmentEvent.DismissPopup) }
        }

        CreateTeamAssignmentPopupType.HELP -> {
            HelpDialog { viewModel.onEvent(CreateTeamAssignmentEvent.DismissPopup) }
        }

        CreateTeamAssignmentPopupType.NONE -> { /* No popup should be shown */
        }

        CreateTeamAssignmentPopupType.CSV_ERROR -> {
            CsvErrorPopup { viewModel.onEvent(CreateTeamAssignmentEvent.DismissPopup) }
        }

        CreateTeamAssignmentPopupType.EXCEL_ERROR -> {
            ExcelErrorPopup { viewModel.onEvent(CreateTeamAssignmentEvent.DismissPopup) }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Csapatbeosztás létrehozása") },
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        NavigateBackIcon()
                    }
                },
                actions = {

                    Box{
                        IconButton(
                            onClick = { menuExpanded.value = !menuExpanded.value }
                        ){
                            Icon(Icons.Filled.MoreVert, null)
                        }

                        DropdownMenu(
                            expanded = menuExpanded.value,
                            onDismissRequest = { menuExpanded.value = false }
                        ){
                            DropdownMenuItem(
                                text = { Text("Importálás CSV-ből") },
                                onClick = {
                                    loadFromCsvLauncher.launch()
                                    menuExpanded.value = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Importálás JSON-ból") },
                                onClick = {
                                    loadFromJsonLauncher.launch()
                                    menuExpanded.value = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Importálás Excelből") },
                                onClick = {
                                    loadFromExcelLauncher.launch()
                                    menuExpanded.value = false
                                }
                            )
                        }
                    }

                    HelpButton(
                        onClick = { viewModel.onEvent(CreateTeamAssignmentEvent.ShowHelp) },
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

                    Row{
                        Text( "Sorszámok kezdete:" )

                        TextField(
                            value = teamAssignment.baseTeamId.toString(),
                            onValueChange = { newBaseId ->
                                viewModel.onEvent(
                                    CreateTeamAssignmentEvent.ChangeBaseTeamId(newBaseId.toIntOrNull() ?: 100)
                                )
                            },
                            singleLine = true
                        )
                    }

                    LazyColumn(
                        state = lazyListState,
                        modifier = Modifier.weight(1f)
                    ) {
                        for (team in teamAssignment.teams) {
                            stickyHeader {
                                Surface(color = Color.Cyan, modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = team.name ?: "${teamAssignment.baseTeamId + teamAssignment.teams.indexOf(team)}",
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
                                        viewModel.onEvent(
                                            CreateTeamAssignmentEvent.ChangeStudentName(
                                                team,
                                                student,
                                                newName
                                            )
                                        )
                                    },
                                    onChangeGroup = { newGroup ->
                                        viewModel.onEvent(
                                            CreateTeamAssignmentEvent.ChangeStudentGroup(
                                                team,
                                                student,
                                                newGroup
                                            )
                                        )
                                    },
                                    onChangeKlass = { newKlass ->
                                        viewModel.onEvent(
                                            CreateTeamAssignmentEvent.ChangeStudentKlass(
                                                team,
                                                student,
                                                newKlass
                                            )
                                        )
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
                                    Row(verticalAlignment = Alignment.CenterVertically) {
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
                                Row(verticalAlignment = Alignment.CenterVertically) {
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