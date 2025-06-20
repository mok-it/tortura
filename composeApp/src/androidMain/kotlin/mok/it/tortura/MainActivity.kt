package mok.it.tortura

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import ca.gosyer.appdirs.impl.attachAppDirs
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.dialogs.init
import kotbase.CouchbaseLite
import mok.it.tortura.util.appDirs
import java.io.File

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FileKit.init(this)

        application.attachAppDirs()

        CouchbaseLite.init( applicationContext, false, File(appDirs.getUserDataDir()), File(appDirs.getUserCacheDir()) )
        setContent {
            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}