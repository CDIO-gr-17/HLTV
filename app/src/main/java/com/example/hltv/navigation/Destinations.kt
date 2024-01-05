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
    val name : String
}

object Home : Destination {
    override val icon: Int = R.drawable.home_24px
    override val route: String = "Home"
    override val screen: @Composable () -> Unit = { HomeScreen() }
    override val name: String = "Home"
}

object Events : Destination {
    override val icon: Int = R.drawable.calendar_month_24px
    override val route: String = "Events"
    override val screen: @Composable () -> Unit = { EventsScreen() }
    override val name: String = "Events"
}
object Matches : Destination {
    override val icon: Int = R.drawable.sports_esports_24px
    override val route: String = "Matches"
    override val screen: @Composable () -> Unit = { /*MatchesScreen()*/ }
    override val name: String = "Matches"
}
object Search : Destination {
    override val icon: Int = R.drawable.search_24px
    override val route: String = "Search"
    override val screen: @Composable () -> Unit = { /*NewsScreen()*/ }
    override val name: String = "Search"
}
object Ranking : Destination {
    override val icon: Int = R.drawable.numbered_list
    override val route: String = "Ranking"
    override val screen: @Composable () -> Unit = {/* MatchesScreen()*/ }
    override val name: String = "Ranking"
}
object Settings : Destination {
    override val icon: Int = R.drawable.settings_24px
    override val route: String = "Settings"
    override val screen: @Composable () -> Unit = { SettingsScreen() }
    override val name: String = "Settings"
}

object SinglePlayer : Destination {
    override val icon: Int = R.drawable.settings_24px
    override val route: String = "Player/{playerID}"
    override val screen: @Composable () -> Unit = { SettingsScreen() }
    override val name :String = "SinglePlayer"
}
object SingleTeam : Destination {
    override val icon: Int = R.drawable.settings_24px
    override val route: String = "Team/{teamID}"
    override val screen: @Composable () -> Unit = { SettingsScreen() }
    override val name :String = "SingleTeam"
}
object SingleMatch : Destination {
    override val icon: Int = R.drawable.settings_24px
    override val route: String = "Match/{matchID}"
    override val screen: @Composable () -> Unit = { SettingsScreen() }
    override val name :String = "SingleMatch"
}
object SingleEvent : Destination {
    override val icon: Int = R.drawable.settings_24px
    override val route: String = "Event/{eventID}"
    override val screen: @Composable () -> Unit = { SettingsScreen() }
    override val name :String = "SingleEvent"
}

val bottomAppBarScreens = listOf(Search, Events, Home, Ranking, Matches)
val allAppScreens = listOf(
    Home,
    Events,
    Matches,
    Search,
    Ranking,
    Settings,
    SinglePlayer,
    SingleTeam,
    SingleMatch,
    SingleEvent
)
