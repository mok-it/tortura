package mok.it.tortura

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import io.github.vinceglb.filekit.FileKit
import kotbase.CouchbaseLite
import mok.it.tortura.util.appDirs
import java.io.File


fun main() = application {
    FileKit.init(appId = "mok.it.tortura")
    CouchbaseLite.init( false, File(appDirs.getUserDataDir()), File(appDirs.getUserCacheDir()) )

    val icon = painterResource("icon.png")

    Window(
        onCloseRequest = ::exitApplication,
        title = "Tortúra",
        icon = icon,
    ) {
        App()
    }
}