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
fun MainMenu(
        onSetUp: (() -> Unit),
        onCompetition: (()->Unit),
        onExit: (()->Unit),
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        Button(onClick = onSetUp ) {
            Text( text = "SetUp")
        }
        Button(onClick = onCompetition ) {
            Text(text="Tortúra!!!")
        }
        Button(onClick = onExit ) {
            Text(text="Jó éjszakát, szevasztok!")
        }

    }
}