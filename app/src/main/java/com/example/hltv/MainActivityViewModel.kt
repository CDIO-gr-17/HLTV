package com.example.hltv

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavBackStackEntry
import com.example.hltv.data.remote.getTeamNameFromID
import com.example.hltv.navigation.Destination
import com.example.hltv.navigation.Events
import com.example.hltv.navigation.Home
import com.example.hltv.navigation.Matches
import com.example.hltv.navigation.News
import com.example.hltv.navigation.Ranking
import com.example.hltv.navigation.Settings
import com.example.hltv.navigation.SinglePlayer
import com.example.hltv.navigation.SingleTeam
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainActivityViewModel() : ViewModel() {
    private val _currentDestination = MutableStateFlow<Destination>(Home)
    val currentDestination: StateFlow<Destination> = _currentDestination

    fun navigateTo(destination: Destination) {
        _currentDestination.value = destination
    }

    val topAppBarTitle = mutableStateOf("Unknown Screen")

    fun getTopAppBarTitle(currentBackStack: NavBackStackEntry?) : String {
        val currentRoute = currentBackStack?.destination?.route
        when (currentRoute) {
            Home.route -> topAppBarTitle.value = "Home"
            Events.route -> topAppBarTitle.value = "Events"
            Matches.route -> topAppBarTitle.value = "Matches"
            News.route -> topAppBarTitle.value = "News"
            Ranking.route -> topAppBarTitle.value = "Ranking"
            Settings.route -> topAppBarTitle.value = "Settings"
            SinglePlayer.route -> topAppBarTitle.value = "Player info"
            SingleTeam.route -> viewModelScope.launch{(currentBackStack.arguments?.getString("teamID"))}
            else -> topAppBarTitle.value = "Unknown Screen"
        }
        return topAppBarTitle.value
    }


/*
    fun getTopAppBarTitle(currentBackStack: NavBackStackEntry?): Any {
        val currentDestination : Destination = currentBackStack?.destination
            return when (currentDestination) {
                is Home -> "Home"
                is Events -> "Events"
                is Matches -> "Matches"
                is News -> "News"
                is Ranking -> "Ranking"
                is Settings -> "Settings"
                is SinglePlayer -> "Player info"
                is SingleTeam -> viewModelScope.launch { CoroutineScope(Dispatchers.IO).launch { getSingleTeamTitle(string) } }

                else -> {
                    "Unknown Screen"
                }
            }

    }
*/
    private suspend fun fetchSingleTeamTitle(teamIDWithPrefix: String?): String {
        var teamID = teamIDWithPrefix?.removePrefix("{teamID}")
        val teamIDInt = teamID?.toInt()
        if (teamIDInt != null) {
            return getTeamNameFromID(teamIDInt)
        } else {
            return "Team info"
        }
    }
}