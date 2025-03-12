package model

import ui.CategoryColors

data class TeamAssignment(
    val category: String,
    val teams: List<Team>,
    val colorSchema: CategoryColors = CategoryColors.UNDIFINED,
)