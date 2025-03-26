package mok.it.tortura

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import mok.it.tortura.navigation.NavGraph
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
@Preview
fun App() {
    MaterialTheme {
        NavGraph()
    }
}