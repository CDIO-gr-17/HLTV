package com.example.hltv.navigation

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.hltv.ui.screens.MatchesScreen.MatchesScreen
import com.example.hltv.ui.screens.eventsScreen.EventsScreen
import com.example.hltv.ui.screens.homeScreen.HomeScreen
import com.example.hltv.ui.screens.newsScreen.NewsScreen
import com.example.hltv.ui.screens.playerScreen.PlayerScreen
import com.example.hltv.ui.screens.settingsScreen.SettingsScreen
import com.example.hltv.ui.screens.singleTeamScreen.SingleTeamScreen


@Composable
fun MainNavHost(navController: NavHostController, modifier: Modifier) {
    NavHost(
        navController = navController,
        startDestination = Home.route,
        modifier = modifier.padding()
    ) {
        composable(route = Home.route){
            HomeScreen()
        }
        composable(route = Events.route) {
            EventsScreen()
        }
        composable(route = Matches.route) {
            MatchesScreen {navController.navigate(SingleTeam.route + it)}
        }
        composable(route = News.route) {
            NewsScreen { navController.navigate(SinglePlayer.route + it) } //How it work? It just no. Ninjutsu
        }
        composable(route = Settings.route) {
            SettingsScreen()
        }
        composable(route = Ranking.route) {
            SettingsScreen()
        }
        composable(route = SingleTeam.route,
            arguments = listOf(navArgument("teamID") { type = NavType.StringType }))
        { backStackEntry ->
            Log.i("MainNavHost", backStackEntry.toString())
            SingleTeamScreen(onClickSinglePlayer = navController.navigate(SinglePlayer.route), teamID = backStackEntry.arguments?.getString("teamID")
        composable(route = SinglePlayer.route,
            arguments = listOf(navArgument("playerID") { type = NavType.StringType }))
        { backStackEntry ->
            Log.i("MainNavHost", backStackEntry.toString())
            PlayerScreen(backStackEntry.arguments?.getString("playerID"))
        }
    }
}