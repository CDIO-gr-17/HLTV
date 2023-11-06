package com.example.hltv.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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
import com.example.hltv.ui.screens.teamsScreen.RankingScreen
import com.example.hltv.ui.screens.settingsScreen.SettingsScreen

@Composable
fun MainNavHost(navController: NavHostController, modifier: Modifier) {
    NavHost(
        navController = navController,
        startDestination = Home.route ,
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
            NewsScreen()
        }
        composable(route = Ranking.route) {
            RankingScreen()
        }
        composable(route = Settings.route) {
            SettingsScreen()
        }
    }
}