package ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ui.components.StudentCard
import viewmodel.CreateTeamAssignmentEvent
import viewmodel.CreateTeamAssignmentViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CreateTeamAssignment(
    onNext: () -> Unit,
) {
    val viewModel = viewModel { CreateTeamAssignmentViewModel() }
    val teamAssignment by remember { viewModel.teamAssignment }

    val lazyListState = rememberLazyListState()

    Row {
        Surface {
            LazyColumn(state = lazyListState) {
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
                        StudentCard(student) {
                            viewModel.onEvent(CreateTeamAssignmentEvent.DeleteMember(team, student))
                        }
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
                        println( "Új csapat ${teamAssignment.teams.size}" )
                        viewModel.onEvent(CreateTeamAssignmentEvent.AddTeam)
                    }) {
                        Row {
                            Icon(Icons.Default.Add, "", modifier = Modifier.size(50.dp))
                            Text("Csapat hozzáadása")
                        }
                    }
                }
                item {
                    Button(onClick = onNext) {
                        Text("Tovább")
                    }
                }
            }
        }
    }
}