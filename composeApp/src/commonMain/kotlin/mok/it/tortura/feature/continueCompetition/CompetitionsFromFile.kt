package mok.it.tortura.feature.continueCompetition

import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.readString
import mok.it.tortura.model.Competition
import mok.it.tortura.util.mapJsonFormat

class CompetitionsFromFile private constructor(
    val file: PlatformFile? = null,
    val competitions : List<Competition>? = null
){
    constructor() : this(null, null)

    suspend fun changeFile(file: PlatformFile): CompetitionsFromFile {
        return CompetitionsFromFile(
            file = file,
            competitions = file.let {
                it.readString().let { json ->
                    mapJsonFormat.decodeFromString<List<Competition>>(json)
                }
            }
        )
    }
}
