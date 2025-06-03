package mok.it.tortura

import io.github.vinceglb.filekit.PlatformFile

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect suspend fun saveStringToFile(file: PlatformFile, string: String)

expect fun goodNightGoodBye()
