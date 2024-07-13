package ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import model.Task

@Composable
fun TaskCard(t: Task, onDeleteTask: () -> Unit) {
    var task by remember { mutableStateOf(t) }
    Card(modifier = Modifier.padding(8.dp)) {
        Column {
            Row {
                TextField(
                    modifier = Modifier.padding(8.dp),
                    value = task.text,
                    onValueChange = { task = task.copy(text = it) },
                    leadingIcon = { Icon(Icons.Filled.Edit, "Feladat szövege") },
                    label = { Text("Feladat szövege") }
                )
                TextField(
                    modifier = Modifier.padding(8.dp),
                    value = task.solution,
                    onValueChange = { task = task.copy(solution = it) },
                    leadingIcon = { Icon(Icons.Filled.Check, "Feladat megoldása") },
                    label = { Text("Feladat megoldása") }
                )
                IconButton(onClick = onDeleteTask) { Icon(Icons.Filled.Delete, "Diák törlése") }
            }
        }
    }
}