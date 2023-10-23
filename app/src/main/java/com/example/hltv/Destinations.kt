package com.example.hltv

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.hltv.screens.EventsScreen
import com.example.hltv.screens.HomeScreen
import com.example.hltv.screens.MatchesScreen
import com.example.hltv.screens.NewsScreen
import com.example.hltv.screens.RankingScreen

interface Destination {
    val icon : ImageVector
    val route : String
    val screen :@Composable () -> Unit
}

object Home : Destination {
    override val icon: ImageVector = Icons.Default.Place
    override val route: String = "home"
    override val screen: @Composable () -> Unit = {HomeScreen()}
}
object Events : Destination {
    override val icon: ImageVector = Icons.Default.Place
    override val route: String = "events"
    override val screen: @Composable () -> Unit = { EventsScreen() }
}
object Matches : Destination {
    override val icon: ImageVector = Icons.Default.Place
    override val route: String = "matches"
    override val screen: @Composable () -> Unit = { MatchesScreen() }
}
object News : Destination {
    override val icon: ImageVector = Icons.Default.Place
    override val route: String = "news"
    override val screen: @Composable () -> Unit = { NewsScreen()}
}
object Ranking : Destination {
    override val icon: ImageVector = Icons.Default.Place
    override val route: String = "ranking"
    override val screen: @Composable () -> Unit = { RankingScreen()}
}

val bottomAppBarRowScreens = listOf(News,Events,Home,Ranking,Matches)