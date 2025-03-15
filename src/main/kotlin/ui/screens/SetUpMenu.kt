package ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun SetUpMenu(
    onCompetitionCreation: () -> Unit = {},
    onTeamCreation: () -> Unit = {},
    onBack: () -> Unit = {}
){
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround,
    ) {
        Button(onClick = onCompetitionCreation) {
            Text("Feladatsor")
        }
        Button(onClick = onTeamCreation) {
            Text("Csapatok")
        }
        Button(onClick = onBack) {
            Text("Vissza")
        }
    }
}
