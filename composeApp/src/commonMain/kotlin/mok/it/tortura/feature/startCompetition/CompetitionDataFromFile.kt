package mok.it.tortura.feature.startCompetition

import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.readString
import kotlinx.serialization.json.Json
import mok.it.tortura.model.ProblemSet
import mok.it.tortura.model.TeamAssignment

class CompetitionDataFromFile private constructor(
    val teamAssignmentFile: PlatformFile?,
    val problemSetFile: PlatformFile?,
    val teamAssignment: TeamAssignment?,
    val problemSet: ProblemSet?,
) {
    constructor() : this(null, null, null, null)

    suspend fun changeTeamAssignmentFile(file: PlatformFile): CompetitionDataFromFile {
        return CompetitionDataFromFile(
            teamAssignmentFile = file,
            teamAssignment = file.let {
                Json.decodeFromString(it.readString())
            },
            problemSet = problemSet,
            problemSetFile = problemSetFile
        )
    }

    suspend fun changeProblemSetFile(file: PlatformFile): CompetitionDataFromFile {
        return CompetitionDataFromFile(
            problemSetFile = file,
            problemSet = file.let {
                Json.decodeFromString(it.readString())
            },
            teamAssignment = teamAssignment,
            teamAssignmentFile = teamAssignmentFile
        )
    }
}