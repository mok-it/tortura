package model

data class Competition(
    val teamAssignment: TeamAssignment,
    val problemSet: ProblemSet,
    val answers: Map<Team,Answer> = teamAssignment.teams.associateWith { Answer(problemSet) }
) {

    fun getTeamCurrentBlock(team: Team) = answers[team]!!.getCurrentBlock()

    fun answerTask( team: Team, task: Task, newAnswer: SolutionState ) : Competition {
        val newAnswers = answers.toMutableMap()
        newAnswers[ team ] = answers[team]!!.answerTask( task, newAnswer )
        return this.copy( answers = newAnswers )
    }
}