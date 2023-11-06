package com.example.hltv.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.hltv.ui.screens.eventsScreen.EventsScreen
import com.example.hltv.ui.screens.homeScreen.HomeScreen
import com.example.hltv.ui.screens.matchesScreen.MatchesScreen
import com.example.hltv.ui.screens.newsScreen.NewsScreen
import com.example.hltv.ui.screens.teamsScreen.RankingScreen
import com.example.hltv.ui.screens.settingsScreen.SettingsScreen

interface Destination {
    val icon : ImageVector
    val route : String
    val screen :@Composable () -> Unit
}

object Home : Destination {
    override val icon: ImageVector = Icons.Default.Home
    override val route: String = "Home"
    override val screen: @Composable () -> Unit = { HomeScreen() }
}
object Events : Destination {
    override val icon: ImageVector = Icons.Default.DateRange
    override val route: String = "Events"
    override val screen: @Composable () -> Unit = { EventsScreen() }
}
object Matches : Destination {
    override val icon: ImageVector = Icons.Default.Create
    override val route: String = "Matches"
    override val screen: @Composable () -> Unit = { MatchesScreen() }
}
object News : Destination {
    override val icon: ImageVector = Icons.Default.MailOutline
    override val route: String = "News"
    override val screen: @Composable () -> Unit = { NewsScreen() }
}
object Ranking : Destination {
    override val icon: ImageVector = Icons.Default.List
    override val route: String = "Ranking"
    override val screen: @Composable () -> Unit = { RankingScreen() }
}
object Settings : Destination {
    override val icon: ImageVector = Icons.Default.Settings
    override val route: String = "Settings"
    override val screen: @Composable () -> Unit = { SettingsScreen() }
}

val bottomAppBarRowScreens = listOf(News, Events, Home, Ranking, Matches)