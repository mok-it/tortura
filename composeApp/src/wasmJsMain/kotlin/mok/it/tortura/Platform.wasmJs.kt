package mok.it.tortura

import io.github.vinceglb.filekit.PlatformFile

class WasmPlatform : Platform {
    override val name: String = "Web with Kotlin/Wasm"
}

actual fun getPlatform(): Platform = WasmPlatform()
actual suspend fun saveStringToFile(file: PlatformFile, string: String) {
    throw RuntimeException()
}

actual fun goodNightGoodBye(){
    js( "alert(\"BOHÓC\")" )
}