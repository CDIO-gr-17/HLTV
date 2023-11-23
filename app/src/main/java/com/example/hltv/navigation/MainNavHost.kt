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
import com.example.hltv.navigation.Events
import com.example.hltv.navigation.Home
import com.example.hltv.navigation.Matches
import com.example.hltv.navigation.News
import com.example.hltv.navigation.Ranking
import com.example.hltv.navigation.Settings
import com.example.hltv.ui.screens.eventsScreen.EventsScreen
import com.example.hltv.ui.screens.homeScreen.HomeScreen
import com.example.hltv.ui.screens.matchesScreen.MatchesScreen
import com.example.hltv.ui.screens.newsScreen.NewsScreen
import com.example.hltv.ui.screens.playerScreen.PlayerScreen
import com.example.hltv.ui.screens.teamsScreen.RankingScreen
import com.example.hltv.ui.screens.settingsScreen.SettingsScreen
import com.example.hltv.ui.screens.teamsScreen.RankingScreenViewModel

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
            MatchesScreen()
        }
        composable(route = News.route) {
            NewsScreen { navController.navigate(SinglePlayer.route + it) } //How it work? It just no. Ninjutsu
        }
        composable(route = Ranking.route) {
            RankingScreen()
        }
        composable(route = Settings.route) {
            SettingsScreen()
        }
        composable(route = SinglePlayer.route,
            arguments = listOf(navArgument("playerID") { type = NavType.StringType }))
        { backStackEntry ->
            Log.i("MainNavHost", backStackEntry.toString())
            PlayerScreen(backStackEntry.arguments?.getString("playerID"))
        }
    }
}