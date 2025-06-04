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
import mok.it.tortura.feature.ongoingCompetition.OngoingCompetition
import mok.it.tortura.feature.startCompetition.StartCompetiton
import mok.it.tortura.goodNightGoodBye

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
                onBack = { navController.popBackStack() },
            )
        }
        composable(Screen.CreateTasks.route) {
            CreateProblemSet(
                onBack = { navController.popBackStack() },
            )
        }
        composable(Screen.SetUpMenu.route) {
            SetUpMenu(
                onCompetitionCreation = { navController.navigate(Screen.CreateTasks.route) },
                onTeamCreation = { navController.navigate(Screen.CreateTeams.route) },
                onBack = { navController.popBackStack() },
            )
        }
        composable(Screen.MainMenu.route) {
            MainMenu(
                onSetUp = { navController.navigate(Screen.SetUpMenu.route) },
                onCompetition = { navController.navigate(Screen.CreateCompetitionFromFile.route) },
                onExit = { goodNightGoodBye() },
            )
        }
        composable(Screen.OngoingCompetition.route) {
            OngoingCompetition(
                onBack = { navController.navigate(Screen.MainMenu.route) },
            )
        }
        composable(Screen.CreateCompetitionFromFile.route) {
            StartCompetiton(
                onBack = { navController.navigate(Screen.MainMenu.route) },
            )
        }
    }
}