package mok.it.tortura

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import navigation.NavGraph
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import tortura.composeapp.generated.resources.Res
//import tortura.composeapp.generated.resources.compose_multiplatform

@Composable
@Preview
fun App() {
    MaterialTheme {
        NavGraph()
    }
}