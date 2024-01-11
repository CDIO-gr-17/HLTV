package com.example.hltv.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.hltv.ui.screens.eventsScreen.EventsScreen
import com.example.hltv.ui.screens.homeScreen.HomeScreen
import com.example.hltv.ui.screens.matchesScreen.MatchesScreen
import com.example.hltv.ui.screens.playerScreen.PlayerScreen
import com.example.hltv.ui.screens.searchScreen.SearchScreen
import com.example.hltv.ui.screens.settingsScreen.SettingsScreen
import com.example.hltv.ui.screens.singleEvent.SingleEventScreen
import com.example.hltv.ui.screens.singleMatch.SingleMatchScreen
import com.example.hltv.ui.screens.singleTeamScreen.SingleTeamScreen


@Composable
fun MainNavHost(navController: NavHostController, modifier: Modifier) {
    NavHost(
        navController = navController,
        startDestination = Home.route,
        modifier = modifier.padding()
    ) {
        composable(route = Home.route) {
            HomeScreen(
                onClickSingleTeam = {navController.navigate(SingleTeam.route + it) },
                onClickSingleMatch = {navController.navigate(SingleMatch.route + it) }
            )
        }

        composable(route = Events.route) {
            EventsScreen(
                onclickSingleEvent = { navController.navigate(SingleEvent.route + it) },
            )
        }

        composable(route = Matches.route) {
            MatchesScreen(
                onClickSingleMatch = { navController.navigate(SingleMatch.route + it)},
                onClickSingleTeam = { navController.navigate(SingleTeam.route + it)})
        }

        composable(route = Search.route) {
            SearchScreen(
                onClickSinglePlayer = { navController.navigate(SinglePlayer.route + it) },
                onClickSingleTeam = { navController.navigate(SingleTeam.route + it) },
                onClickSingleTournament = { navController.navigate(SingleEvent.route + it)})  //How it work? It just no. Ninjutsu
        }

        composable(route = Settings.route) {
            SettingsScreen()
        }

        composable(route = Ranking.route) {
            SettingsScreen()
        }

        composable(route = SingleTeam.route + "{teamID}",
            arguments = listOf(navArgument("teamID") { type = NavType.StringType }))
        { backStackEntry ->
            SingleTeamScreen(
                teamID = backStackEntry.arguments?.getString("teamID"),
                onClickSinglePlayer = {navController.navigate(SinglePlayer.route + it)},
                onClickSingleTeam = {navController.navigate(SingleTeam.route + it) },
                onClickSingleMatch = {navController.navigate(SingleMatch.route + it) })
        }

        composable(
            route = SingleMatch.route + "{matchID}",
            arguments = listOf(navArgument("matchID") { type = NavType.StringType }))
        { backStackEntry ->
            SingleMatchScreen(
                matchID = backStackEntry.arguments?.getString("matchID"),
                onClickSingleTeam = {navController.navigate(SingleTeam.route + it) })
        }

        composable(
            route = SinglePlayer.route + "{playerID}",
            arguments = listOf(navArgument("playerID") { type = NavType.StringType })
        )
        { backStackEntry ->
            PlayerScreen(backStackEntry.arguments?.getString("playerID"))
        }
        composable(route = SingleEvent.route + "{eventID}"+"/"+"{seasonID}",
            arguments = listOf(navArgument("eventID") { type = NavType.StringType }, navArgument("seasonID") { type = NavType.StringType }))
        { backStackEntry ->
            SingleEventScreen(
                tournamentID = backStackEntry.arguments?.getString("eventID"),
                seasonID = backStackEntry.arguments?.getString("seasonID"),
                onClickSingleTeam = {navController.navigate(SingleTeam.route + it) },
                onClickSingleMatch = {navController.navigate(SingleMatch.route + it) }

            )
        }
    }
}