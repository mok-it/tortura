package mok.it.tortura.model

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable


@Serializable
data class Competition(
    val teamAssignment: TeamAssignment,
    val problemSet: ProblemSet,
    val answers: List<CompetitionTeam> = teamAssignment.teams.map { team -> CompetitionTeam( team, Answer(problemSet)) },
    val startTime: Instant = Clock.System.now(),
    val id: String? = null,
) {

    private fun modifyTeam(competitionTeam: CompetitionTeam, newAnswer: Answer): Competition {
        val newAnswers = answers.toMutableList()
        val index = answers.indexOf(competitionTeam)
        val newCompetitionTeam = competitionTeam.copy( answer = newAnswer )
        newAnswers.removeAt(index)
        newAnswers.add(index, newCompetitionTeam)
        return this.copy(answers = newAnswers)
    }

    fun answerTask(competitionTeam: CompetitionTeam, task: Task, newAnswer: SolutionState) : Competition {
        return modifyTeam( competitionTeam, competitionTeam.answer.answerTask( task, newAnswer ) )
    }

    fun restartCurrentBlock(competitionTeam: CompetitionTeam): Competition {
        return modifyTeam(competitionTeam, competitionTeam.answer.restartCurrentBlock())
    }

    fun nextBlock(competitionTeam: CompetitionTeam): Competition {
        return modifyTeam(competitionTeam, competitionTeam.answer.nextBlock())
    }

    fun navigateBackwards(competitionTeam: CompetitionTeam): Competition {
        return modifyTeam( competitionTeam, competitionTeam.answer.navigateBackwards() )
    }

    fun navigateForwards(competitionTeam: CompetitionTeam): Competition {
        return modifyTeam( competitionTeam, competitionTeam.answer.navigateForwards() )
    }

    fun deleteLastTry(competitionTeam: CompetitionTeam): Competition {
        return modifyTeam( competitionTeam, competitionTeam.answer.deleteLastTry() )
    }
}
