package ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun Menu( onCompetitionCreation: () -> Unit = {},
          onTeamCreation: () -> Unit = {},
          onExit: () -> Unit = {},){
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround,
    ) {
        Button(onClick = onCompetitionCreation) {
            Text("Competition")
        }
        Button(onClick = onTeamCreation) {
            Text("Team")
        }
        Button(onClick = onExit) {
            Text("Exit")
        }
        SolveBlockPreview()
    }
}
