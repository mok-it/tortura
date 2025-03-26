package mok.it.tortura.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import mok.it.tortura.feature.MainMenu
import mok.it.tortura.feature.SetUpMenu
import mok.it.tortura.feature.createProblemSet.CreateProblemSet
import mok.it.tortura.feature.createTeamAssigment.CreateTeamAssignment
import mok.it.tortura.feature.onGoingCompetition.OngoingCompetition

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.MainMenu.route
    ) {
        composable(Screen.CreateTeams.route) {
            CreateTeamAssignment(
                onNext = { navController.navigate(Screen.CreateTasks.route) },
            )
        }
        composable(Screen.CreateTasks.route) {
            CreateProblemSet()
        }
        composable(Screen.SetUpMenu.route) {
            SetUpMenu(
                onCompetitionCreation = { navController.navigate(Screen.CreateTasks.route) },
                onTeamCreation = { navController.navigate(Screen.CreateTeams.route) },
                onBack = { navController.navigate(Screen.MainMenu.route) }
            )
        }
        composable(Screen.MainMenu.route) {
            MainMenu(
                onSetUp = { navController.navigate(Screen.SetUpMenu.route) },
                onCompetition = { navController.navigate(Screen.OngoingCompetition.route) },
                onExit = { /*exitProcess(0)*/ },
            )
        }
        composable(Screen.OngoingCompetition.route) {
            OngoingCompetition(
                onBack = { navController.navigate(Screen.MainMenu.route) },
            )
        }
    }
}