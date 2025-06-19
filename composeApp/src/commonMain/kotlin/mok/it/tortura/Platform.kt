package mok.it.tortura

import io.github.vinceglb.filekit.PlatformFile
import mok.it.tortura.model.ProblemSet

interface Platform {
    val name: String
    val excelImportImplemented: Boolean
}

expect fun getPlatform(): Platform

expect suspend fun saveStringToFile(file: PlatformFile, string: String)

expect fun loadProblemSetFromExcel( file: PlatformFile ): ProblemSet?

expect fun goodNightGoodBye()
