package mok.it.tortura.model

import kotlinx.serialization.Serializable

@Serializable
data class Competition(
    val teamAssignment: TeamAssignment,
    val problemSet: ProblemSet,
    val answers: Map<Team, Answer> = teamAssignment.teams.associateWith { Answer(problemSet) }
) {

    fun answerTask(team: Team, task: Task, newAnswer: SolutionState) : Competition {
        return modifyTeam( team, answers[team]!!.answerTask( task, newAnswer ) )
    }

    fun restartCurrentBlock(team: Team): Competition {
        return modifyTeam(team, answers[team]!!.restartCurrentBlock())
    }

    fun nextBlock(team: Team): Competition {
        return modifyTeam(team, answers[team]!!.nextBlock())
    }

    private fun modifyTeam(team: Team, newAnswer: Answer): Competition {
        val newAnswers = answers.toMutableMap()
        newAnswers[team] = newAnswer
        return this.copy(answers = newAnswers)
    }

    fun navigateBackwards(team: Team): Competition {
        return modifyTeam( team, answers[team]!!.navigateBackwards() )
    }

    fun navigateForwards(team: Team): Competition {
        return modifyTeam( team, answers[team]!!.navigateForwards() )
    }
}