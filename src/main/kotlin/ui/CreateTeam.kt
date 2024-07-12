package ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import model.Competition
import model.Student
import model.Team
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState
import viewmodel.CreateTeamViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CreateTeam(competition: Competition) {
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
                            Row {
                                Text(team.id.toString())
                                //Button(onClick = { teams.remove(team) }) {}
                                IconButton(onClick = { teams.remove(team) }, modifier = Modifier.size(90.dp)) {
                                    Icon(Icons.Filled.Delete, "asdf")
                                }
                            }

                        }
                    }

                    itemsIndexed(team.students, key = { _, item -> item.id }) { index, item ->
                        ReorderableItem(reorderableLazyListState, key = item.id) { isDragging ->
                            // Item content

                            StudentCard(item)
                        }
                    }
                    item {
                        Button(
                            onClick = {
                                teams.remove(team)
                                team.students.add(Student(""))
                                teams.add(team)
                            }
                        ) {

                        }
                    }
                }
            }
        }

        Column {
            Button(
                onClick = {
                    val lastTeam = teams.last()
                    teams.removeLast()
                    lastTeam.students.add(Student(""))
                    teams.add(lastTeam)
                }
            ) {
                Text("Add Student")
            }
            Button(onClick = {
                teams.add(Team(viewModel.newTeamId(), mutableListOf(), competition))
            }) {
                Text("Add Team")
            }
        }
    }

}