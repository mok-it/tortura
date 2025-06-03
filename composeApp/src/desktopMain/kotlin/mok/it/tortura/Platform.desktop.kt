package mok.it.tortura

import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.writeString
import kotlin.system.exitProcess

actual suspend fun saveStringToFile(file: PlatformFile, string: String) {
    file.writeString(string)
}

actual fun goodNightGoodBye() {
    exitProcess( 0 )
}