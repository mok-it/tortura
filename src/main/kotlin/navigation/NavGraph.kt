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
import ui.Menu
import kotlin.system.exitProcess

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Menu.route
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
            CreateTask()
        }
        composable(Screen.Menu.route) {
            Menu(
                onCompetitionCreation = { navController.navigate(Screen.CreateTasks.route) },
                onTeamCreation = { navController.navigate(Screen.CreateTeams.route) },
                onExit = { exitProcess(0) }
            )
        }
    }
}