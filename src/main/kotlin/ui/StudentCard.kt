package ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Card
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import model.Student

@Composable
fun StudentCard(s: Student) {
    var student by remember { mutableStateOf(s) }

    Card {
        Column {
            TextField(
                value = student.name,
                onValueChange = { student = student.copy(name = it) }
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