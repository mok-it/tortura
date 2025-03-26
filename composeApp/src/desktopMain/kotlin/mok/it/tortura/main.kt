package mok.it.tortura

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import io.github.vinceglb.filekit.FileKit

fun main() = application {
    FileKit.init(appId = "mok.it.tortura")
    Window(
        onCloseRequest = ::exitApplication,
        title = "tortura",
    ) {
        App()
    }
}