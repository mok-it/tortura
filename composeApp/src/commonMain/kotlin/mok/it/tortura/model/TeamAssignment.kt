package mok.it.tortura.model

import kotlinx.serialization.Serializable

@Serializable
data class TeamAssignment(
    val teams: List<Team>,
    val baseTeamId : Int = 100,
)