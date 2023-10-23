package com.example.hltv

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun mainNavHost(navController: NavHostController,modifier: Modifier) {
    NavHost(
        navController = navController,
        startDestination = Home.route ,
        modifier = modifier.padding()
    ) {
        composable(route = Home.route){
            Home.screen
        }
        composable(route = Events.route) {
            Events.screen
        }
        composable(route = Matches.route) {
            Matches.screen
        }
        composable(route = News.route) {
            News.screen
        }
        composable(route = Ranking.route) {
            Ranking.screen
        }
    }

}