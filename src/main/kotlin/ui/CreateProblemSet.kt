package ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import viewmodel.CompetitionEditEvent
import viewmodel.CreateTaskViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CreateProblemSet(
) {
    val viewModel: CreateTaskViewModel = viewModel { CreateTaskViewModel() }
    val competition by remember { viewModel.competition }
    LazyColumn {
        for (block in competition.blocks) {
            stickyHeader {
                Surface(color = Color.Cyan, modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            "${competition.blocks.indexOf(block) + 1}. blokk ${block.id}",
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
                    onChangeText = { text -> viewModel.onEvent(CompetitionEditEvent.ChangeTaskText(block, it, text)) },
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
//                        teams.remove(team)
//                        team.students.add(Student(""))
//                        teams.add(team)
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
}
