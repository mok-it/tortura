package navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import model.Block
import model.ProblemSet
import model.Task
import ui.CreateProblemSet
import ui.CreateTeamAssignment
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
            CreateTeamAssignment(
                onNext = { navController.navigate(Screen.CreateTasks.route) },
                competition = ProblemSet(
                    "ABC",
                    listOf(
                        Block(listOf(Task("Micimackó?", "42")), 1)
                    ),
                )
            )
        }
        composable(Screen.CreateTasks.route) {
            CreateProblemSet()
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