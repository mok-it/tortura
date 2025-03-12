package ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import model.Competition
import viewmodel.OngoingCompetitionViewModel

@Composable
fun OngoingCompetition() {

    val viewModel = OngoingCompetitionViewModel()

    fun teamsInPreviousCompetitions( competition: Competition): Int{
        var counter = 0
        for( c in viewModel.competitions ){
            if( c == competition ) return counter
            counter += c.teamAssignment.teams.size
        }
        return counter
    }

    var tabIndex by remember { mutableStateOf(0) }

//    val tabs = mutableListOf<String>()
//
//    for( i in viewModel.competitions.indices ) {
//        val teams = viewModel.competitions[ i ].teamAssignment.teams
//        teams.forEach{ team->
//            tabs.add( "${ i * 100 + teams.indexOf( team) } + 1" )
//        }
//    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        for( competition in viewModel.competitions ) {
            val teams = competition.teamAssignment.teams
            teams.forEachIndexed { index, team ->
                if( tabIndex == teamsInPreviousCompetitions(competition) + index ) {
                    AnswerBlock(
                        competition.getTeamCurrentBlock(team),
                        0,
                        { _,_ ->  },
                        {},
                        competition.teamAssignment.colorSchema.textColor,
                        competition.teamAssignment.colorSchema.backgroundColor,
                    )
                }
            }
        }
        ScrollableTabRow(
            selectedTabIndex = tabIndex,
            backgroundColor = Color.Transparent,
        ) {
            for( competition in viewModel.competitions ){
                val teams = competition.teamAssignment.teams
                teams.forEachIndexed{ index, _->
                    Tab(
                        selected = tabIndex == teamsInPreviousCompetitions(competition) + index,
                        onClick = { tabIndex = teamsInPreviousCompetitions(competition) + index },
                        text= {
                            Text(
                                text = "${ (viewModel.competitions.indexOf(competition) + 1) * 100 + index }",
                                color = competition.teamAssignment.colorSchema.textColor,
                            )
                              },
                        modifier = Modifier.background(competition.teamAssignment.colorSchema.backgroundColor),
                    )
                }
            }
        }
    }
}
