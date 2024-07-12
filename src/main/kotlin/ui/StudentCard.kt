package ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import model.Student

@Composable
fun StudentCard(s: Student) {
    var student by remember { mutableStateOf(s) }

    Card {
        Column {
            TextField(
                modifier = Modifier.padding(8.dp),
                value = student.name,
                onValueChange = { student = student.copy(name = it) },
                enabled = true,
                leadingIcon = { Icon(Icons.Filled.Person, "asdf") }
            )
            Row {
                TextField(
                    value = student.group,
                    onValueChange = { student = student.copy(group = it) }
                )
                TextField(
                    value = student.klass,
                    onValueChange = { student = student.copy(klass = it) }
                )
            }
        }
    }
}