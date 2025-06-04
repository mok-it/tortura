package mok.it.tortura.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import mok.it.tortura.model.Student

@Composable
fun StudentCard(s: Student, onDeleteStudent: () -> Unit) {
    var student by remember { mutableStateOf(s) }

    Card(modifier = Modifier.padding(8.dp)) {
        Column {
            Row {
                TextField(
                    modifier = Modifier.padding(8.dp),
                    value = student.name,
                    onValueChange = { student = student.copy(name = it) },
                    leadingIcon = { Icon(Icons.Filled.Person, "Diák neve") },
                    label = { Text("Diák neve") }
                )
                IconButton(onClick = onDeleteStudent) { Icon(Icons.Filled.Delete, "Diák törlése") }
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