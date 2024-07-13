package ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import model.Student

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun StudentCard(s: Student, interactionSource: MutableInteractionSource, onDeleteStudent: () -> Unit) {
    var student by remember { mutableStateOf(s) }

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
                    onValueChange = { student = student.copy(name = it) },
                    enabled = true,
                    leadingIcon = { Icon(Icons.Filled.Person, "Diák neve") },
                    label = { Text("Diák neve") }
                )
                IconButton(onClick = onDeleteStudent) { Icon(Icons.Filled.Delete, "Diák törlése") }
//                IconButton(
//                    modifier = Modifier
//                        .draggableHandle(
//                            onDragStarted = {
//                            },
//                            onDragStopped = {
//                            },
//                            interactionSource = interactionSource,
//                        )
//                        .clearAndSetSemantics { },
//                    onClick = {},
//                ) {
//                    Icon(Icons.Rounded.Settings, contentDescription = "Reorder")
//                }
            }
            Row {
                TextField(
                    modifier = Modifier.padding(8.dp),
                    value = student.group,
                    leadingIcon = { Icon(Icons.Filled.Home, "Csoport") },
                    onValueChange = { student = student.copy(group = it) },
                    label = { Text("Csoport") }
                )
                TextField(
                    modifier = Modifier.padding(8.dp),
                    value = student.klass,
                    leadingIcon = { Icon(Icons.Filled.DateRange, "Osztály") },
                    onValueChange = { student = student.copy(klass = it) },
                    label = { Text("Osztály") }
                )
            }
        }
    }
}