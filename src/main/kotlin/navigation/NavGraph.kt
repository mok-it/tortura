package navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import model.Block
import model.Competition
import model.Task
import ui.CreateTask
import ui.CreateTeam

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.CreateTeams.route
    ) {
        composable(Screen.CreateTeams.route) {
            CreateTeam(
                onNext = { navController.navigate(Screen.CreateTasks.route) },
                competition = Competition(
                    "ABC",
                    listOf(
                        Block(listOf(Task("Micimackó?", "42")), 1)
                    ),
                )
            )
        }
        composable(Screen.CreateTasks.route) {
            CreateTask(Competition("", emptyList()))
        }
    }
}