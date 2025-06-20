package mok.it.tortura

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import io.github.vinceglb.filekit.FileKit
import kotbase.CouchbaseLite
import mok.it.tortura.util.appDirs
import java.io.File

fun main() = application {
    FileKit.init(appId = "mok.it.tortura")
    CouchbaseLite.init( false, File(appDirs.getUserDataDir()), File(appDirs.getUserCacheDir()) )
    Window(
        onCloseRequest = ::exitApplication,
        title = "tortura",
    ) {
        App()
    }
}