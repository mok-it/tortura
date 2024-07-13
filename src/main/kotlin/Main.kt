import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import navigation.NavGraph

@Composable
@Preview
fun App() {

    MaterialTheme {
        NavGraph()
//        CreateTeam(
//            Competition(
//                "ABC", listOf(
//                    Block(listOf(Task("Micimackó?", "42")), 1)
//                )
//            )
//        )
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
