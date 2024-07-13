
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import model.Block
import model.Competition
import model.Task
import ui.CreateTeam2

@Composable
@Preview
fun App() {

    MaterialTheme {
        CreateTeam2(
            Competition(
                "ABC", listOf(
                    Block(listOf(Task("Micimackó?", "42")), 1)
                )
            )
        )

//        SimpleReorderableLazyColumnScreen()
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
