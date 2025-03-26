package mok.it.tortura.model

import mok.it.tortura.ui.CategoryColors

data class TeamAssignment(
    val category: String,
    val teams: List<Team>,
    val colorSchema: CategoryColors = CategoryColors.UNDIFINED,
)