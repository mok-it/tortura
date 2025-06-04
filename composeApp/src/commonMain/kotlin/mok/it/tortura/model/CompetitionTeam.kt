package mok.it.tortura.model

import kotlinx.serialization.Serializable

@Serializable
data class CompetitionTeam(
    val team: Team,
    val answer: Answer,
)
