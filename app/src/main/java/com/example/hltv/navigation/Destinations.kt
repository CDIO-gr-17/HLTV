package com.example.hltv.navigation

import com.example.hltv.R

interface Destination {
    val icon : Int
    val route : String
    val name : String
}

object Home : Destination {
    override val icon: Int = R.drawable.home_24px
    override val route: String = "Home"
    override val name: String = "Home"
}

object Events : Destination {
    override val icon: Int = R.drawable.calendar_month_24px
    override val route: String = "Events"
    override val name: String = "Events"
}
object Matches : Destination {
    override val icon: Int = R.drawable.sports_esports_24px
    override val route: String = "Matches"
    override val name: String = "Matches"
}
object Search : Destination {
    override val icon: Int = R.drawable.search_24px
    override val route: String = "Search"
    override val name: String = "Search"
}
object Ranking : Destination {
    override val icon: Int = R.drawable.numbered_list
    override val route: String = "Ranking"
    override val name: String = "Ranking"
}
object Settings : Destination {
    override val icon: Int = R.drawable.settings_24px
    override val route: String = "Settings"
    override val name: String = "Settings"
}

object SinglePlayer : Destination {
    override val icon: Int = R.drawable.settings_24px
    override val route: String = "Player/"
    override val name :String = "SinglePlayer"
}
object SingleTeam : Destination {
    override val icon: Int = R.drawable.settings_24px
    override val route: String = "Team/"
    override val name :String = "SingleTeam"
}
object SingleMatch : Destination {
    override val icon: Int = R.drawable.settings_24px
    override val route: String = "Match/"
    override val name :String = "SingleMatch"
}
object SingleEvent : Destination {
    override val icon: Int = R.drawable.settings_24px
    override val route: String = "Event/"
    override val name :String = "SingleEvent"
}

val bottomAppBarScreens = listOf(Home, Search, Events,  Matches)
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
