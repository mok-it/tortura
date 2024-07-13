package navigation

sealed class Screen(val route: String) {
    object CreateTeams : Screen("create_teams")
    object CreateTasks : Screen("create_tasks")
    object OngoingCompetition : Screen("ongoing_competition")
}