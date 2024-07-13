package ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.lifecycle.viewmodel.compose.viewModel
import model.Competition
import model.Student
import model.Team
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState
import viewmodel.CreateTeamViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CreateTeam(
    competition: Competition,
    onNext: () -> Unit,
    viewModel: CreateTeamViewModel = viewModel { CreateTeamViewModel(competition) }
) {
    //val viewModel = CreateTeamViewModel(competition)
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

                            StudentCard(student) {
                                teams.remove(team)
                                val students = team.students
                                students.remove(student)
                                teams.add(Team(team.id, students, team.competition))
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
                item {
                    Button(onClick = onNext) {
                        Text("Tovább")
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

