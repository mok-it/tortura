package mok.it.tortura

import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.writeString

actual suspend fun saveStringToFile(file: PlatformFile, string: String) {
    file.writeString(string)
}