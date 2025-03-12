package model

class Competition( val teamAssignment: TeamAssignment, val problemSet: ProblemSet ) {
    val answers: MutableMap<Team,Answer> = mutableMapOf()

    init {
        for( team in teamAssignment.teams ) {
            answers[team] = Answer( problemSet )
        }
    }

    fun getTeamCurrentBlock(team: Team) = answers[team]!!.getCurrentBlock()
}