package com.example.hltv.navigation

import androidx.compose.runtime.Composable
import com.example.hltv.R
import com.example.hltv.ui.screens.eventsScreen.EventsScreen
import com.example.hltv.ui.screens.homeScreen.HomeScreen
import com.example.hltv.ui.screens.settingsScreen.SettingsScreen

interface Destination {
    val icon : Int
    val route : String
    val screen :@Composable () -> Unit
}

object Home : Destination {
    override val icon: Int = R.drawable.home_24px
    override val route: String = "Home"
    override val screen: @Composable () -> Unit = { HomeScreen() }
}
object Events : Destination {
    override val icon: Int = R.drawable.calendar_month_24px
    override val route: String = "Events"
    override val screen: @Composable () -> Unit = { EventsScreen() }
}
object Matches : Destination {
    override val icon: Int = R.drawable.sports_esports_24px
    override val route: String = "Matches"
    override val screen: @Composable () -> Unit = { /*MatchesScreen()*/ }
}
object News : Destination {
    override val icon: Int = R.drawable.newspaper_24px
    override val route: String = "News"
    override val screen: @Composable () -> Unit = { /*NewsScreen()*/ }
}
object Ranking : Destination {
    override val icon: Int = R.drawable.numbered_list
    override val route: String = "Ranking"
    override val screen: @Composable () -> Unit = {/* MatchesScreen()*/ }
}
object Settings : Destination {
    override val icon: Int = R.drawable.settings_24px
    override val route: String = "Settings"
    override val screen: @Composable () -> Unit = { SettingsScreen() }
}

object SinglePlayer : Destination {
    override val icon: Int = R.drawable.settings_24px
    override val route: String = "Player/{playerID}"
    override val screen: @Composable () -> Unit = { SettingsScreen() }
}
object SingleTeam : Destination {
    override val icon: Int = R.drawable.settings_24px
    override val route: String = "Team/{teamID}"
    override val screen: @Composable () -> Unit = { SettingsScreen() }
}

    val bottomAppBarScreens = listOf(News, Events, Home, Ranking, Matches)
