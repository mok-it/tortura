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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import model.Competition
import viewmodel.CreateTaskViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CreateTask(
    competition: Competition,
    viewModel: CreateTaskViewModel = viewModel { CreateTaskViewModel() }
) {
    val blocks = remember { viewModel.blocks }
    LazyColumn {
        for (block in competition.blocks) {
            stickyHeader {
                Surface(color = Color.Cyan, modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("${blocks.indexOf(block) + 1}. blokk", modifier = Modifier.padding(end = 8.dp))
                        IconButton(onClick = {
                            viewModel.deleteBlock(block)
                        }, modifier = Modifier.size(40.dp)) {
                            Icon(Icons.Filled.Delete, "Blokk törlése")
                        }
                    }

                }
            }
            items(block.tasks) {
                TaskCard(it) {
                    viewModel.deleteTask(it, block)
                }
            }
            item {
                Button(
                    onClick = {
                        viewModel.addTask(block)
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
                viewModel.newBlock()
            }) {
                Row {
                    Icon(Icons.Default.Add, "", modifier = Modifier.size(50.dp))
                    Text("Blokk hozzáadása")
                }
            }
        }
    }
}