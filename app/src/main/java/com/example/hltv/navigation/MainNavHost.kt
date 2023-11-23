package com.example.hltv.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.hltv.ui.screens.eventsScreen.EventsScreen
import com.example.hltv.ui.screens.homeScreen.HomeScreen
import com.example.hltv.ui.screens.newsScreen.NewsScreen
import com.example.hltv.ui.screens.teamsScreen.RankingScreen
import com.example.hltv.ui.screens.settingsScreen.SettingsScreen
import com.example.hltv.ui.screens.singleMatch.SingleMatchScreen

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
           SingleMatchScreen()
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