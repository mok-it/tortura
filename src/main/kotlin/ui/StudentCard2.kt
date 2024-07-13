package ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.unit.dp
import model.Competition
import model.Student
import model.Team
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState
import viewmodel.CreateTeamViewModel

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun CreateTeam2(competition: Competition) {
    val viewModel = CreateTeamViewModel(competition)
    val teams = remember { viewModel.teams }

    val lazyListState = rememberLazyListState()
    val reorderableLazyListState = rememberReorderableLazyListState(lazyListState) { from, to ->
        viewModel.moveStudent(from, to)
    }

    Row {
        Surface {
            LazyColumn(state = lazyListState) {

                for (team in teams.sortedBy { it.id }) {
                    stickyHeader {
                        Surface(color = Color.Cyan, modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("${team.id}. csapat", modifier = Modifier.padding(end = 8.dp))
                                IconButton(onClick = {
                                    teams.remove(team)
                                    for (i in 0..<teams.size) {
                                        if (teams[i].id > team.id) {
                                            val t = teams.removeAt(i)
                                            teams.add(i, Team(t.id - 1, t.students, t.competition))
                                        }
                                    }
                                }, modifier = Modifier.size(40.dp)) {
                                    Icon(Icons.Filled.Delete, "Csapat törlése")
                                }
                            }

                        }
                    }

                    itemsIndexed(team.students, key = { _, student -> student.id }) { index, student ->
                        ReorderableItem(reorderableLazyListState, key = student.id) { isDragging ->

                            val interactionSource = remember { MutableInteractionSource() }

                            Card(
                                onClick = {},
                                modifier = Modifier.padding(8.dp),
                                interactionSource = interactionSource
                            ) {
                                Column {
                                    Row {
                                        TextField(
                                            modifier = Modifier.padding(8.dp),
                                            value = student.name,
                                            onValueChange = { /*student = student.copy(name = it)*/ },
                                            enabled = true,
                                            leadingIcon = { Icon(Icons.Filled.Person, "Diák neve") },
                                            label = { Text("Diák neve") }
                                        )
                                        IconButton(onClick = {  }) { Icon(Icons.Filled.Delete, "Diák törlése") }
                IconButton(
                    modifier = Modifier
                        .draggableHandle(
                            onDragStarted = {
                            },
                            onDragStopped = {
                            },
                            interactionSource = interactionSource,
                        )
                        .clearAndSetSemantics { },
                    onClick = {},
                ) {
                    Icon(Icons.Rounded.Settings, contentDescription = "Reorder")
                }
                                    }
                                    Row {
                                        TextField(
                                            modifier = Modifier.padding(8.dp),
                                            value = student.group,
                                            leadingIcon = { Icon(Icons.Filled.Home, "Csoport") },
                                            onValueChange = { /*student = student.copy(group = it)*/ },
                                            label = { Text("Csoport") }
                                        )
                                        TextField(
                                            modifier = Modifier.padding(8.dp),
                                            value = student.klass,
                                            leadingIcon = { Icon(Icons.Filled.DateRange, "Osztály") },
                                            onValueChange = { /*student = student.copy(klass = it)*/ },
                                            label = { Text("Osztály") }
                                        )
                                    }
                                }
                            }
                        }
                    }
                    item {
                        Button(
                            onClick = {
                                teams.remove(team)
                                team.students.add(Student(""))
                                teams.add(team)
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
                        teams.add(Team(viewModel.newTeamId(), mutableListOf(), competition))
                    }) {
                        Row {
                            Icon(Icons.Default.Add, "", modifier = Modifier.size(50.dp))
                            Text("Csapat hozzáadása")
                        }
                    }
                }
            }
        }
    }

    fun deleteTeam(team: Team) {
        teams.remove(team)
        for (t in teams) {
            if (t.id > team.id) {
                teams.remove(t)
                teams.add(Team(t.id - 1, t.students, t.competition))
            }
        }
    }
}

