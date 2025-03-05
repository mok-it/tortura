package ui

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import model.Competition
import model.Student
import model.Team
import viewmodel.CreateTeamViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CreateTeam(
    competition: Competition,
    onNext: () -> Unit,
) {
    val viewModel = CreateTeamViewModel(competition)
    val teams = remember { viewModel.teams }

    val lazyListState = rememberLazyListState()

    Row {
        Surface {
            LazyColumn(state = lazyListState) {
                for (team in teams) {
                    stickyHeader {
                        Surface(color = Color.Cyan, modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("${teams.indexOf(team) + 1}. csapat", modifier = Modifier.padding(end = 8.dp))
                                IconButton(onClick = {
                                    teams.remove(team)
                                }, modifier = Modifier.size(40.dp)) {
                                    Icon(Icons.Filled.Delete, "Csapat törlése")
                                }
                            }
                        }
                    }

                    items(team.students) { student ->

                        StudentCard(student) {
                            teams.remove(team)
                            val students = team.students
                            students.remove(student)
                            teams.add(Team(students))
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


                    item {
                        Button(shape = CircleShape, onClick = {
                            teams.add(Team(mutableListOf()))
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
}

