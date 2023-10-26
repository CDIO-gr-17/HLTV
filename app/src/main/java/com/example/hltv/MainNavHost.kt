package com.example.hltv

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.hltv.screens.EventsScreen
import com.example.hltv.screens.HomeScreen
import com.example.hltv.screens.MatchesScreen
import com.example.hltv.screens.NewsScreen
import com.example.hltv.screens.RankingScreen
import com.example.hltv.screens.SettingsScreen

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