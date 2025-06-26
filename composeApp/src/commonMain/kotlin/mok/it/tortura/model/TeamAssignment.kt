package mok.it.tortura.model

import kotlinx.serialization.Serializable
import mok.it.tortura.ui.CategoryColors

@Serializable
data class TeamAssignment(
    val category: String,
    val teams: List<Team>,
    val baseTeamId : Int = 100,
    val colorSchema: CategoryColors = CategoryColors.UNDIFINED,
)