package com.example.hltv.ui.screens.matchesScreen

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hltv.data.convertTimestampToDateURL
import com.example.hltv.data.remote.Event
import com.example.hltv.data.remote.getLiveMatches
import com.example.hltv.data.remote.getMatchesFromDay
import com.example.hltv.data.remote.getTeamImage
import com.example.hltv.data.remote.getTournamentLogo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class MatchesScreenViewModel : ViewModel() {
    val liveMatchesValues = mutableStateListOf<Event>()
    val upcomingMatchesValues = mutableStateListOf<Event>()
    val tournamentValues = mutableStateListOf<Event>()

    private var upcomingMatchIndex = 0
    private var tournamentIndex = 0

    val awayTeamIcons = MutableList<Bitmap?>(999) { null }
    val homeTeamIcons = MutableList<Bitmap?>(999) { null }
    val tournamentIcons = MutableList<Bitmap?>(999) { null }

    private var nextDayInSeconds = System.currentTimeMillis() / 1000

    private val _loadingState = MutableStateFlow(true)
    val loadingState: StateFlow<Boolean> get() = _loadingState

    private var dataLoaded = false
    suspend fun loadUpcomingMatches() {

        _loadingState.value = true
        viewModelScope.launch(Dispatchers.IO) {
            var maxNumberOfDaysToLoad =
                15 //Prevent infinite loading in case theres no more tournaments, jamming the API
            do {
                val upcomingMatches =
                    getMatchesFromDay(convertTimestampToDateURL((nextDayInSeconds).toInt()))

                var dayWasEmpty = true
                if (upcomingMatches.events.isNotEmpty()) {
                    upcomingMatches.events = upcomingMatches.events.sortedBy { it.startTimestamp }
                    for (event in upcomingMatches.events) {
                        if (event.startTimestamp?.toLong() != null &&  //Makes sure that the upcoming match has an associated startTimestamp
                            event.startTimestamp!! > (System.currentTimeMillis() / 1000) && //Excludes matches where the startTimestamp has passed (i.e it is live or has been played)
                            event !in upcomingMatchesValues
                        ) {
                            upcomingMatchesValues.add(upcomingMatchIndex, event)
                            tournamentValues.add(tournamentIndex, event)
                            homeTeamIcons[liveMatchesValues.size + upcomingMatchIndex] =
                                (getTeamImage(event.homeTeam.id))
                            awayTeamIcons[liveMatchesValues.size + upcomingMatchIndex] =
                                (getTeamImage(event.awayTeam.id))
                            tournamentIcons[tournamentIndex] =
                                getTournamentLogo(event.tournament.uniqueTournament?.id)
                            upcomingMatchIndex++
                            tournamentIndex++
                            dayWasEmpty = false
                        }
                    }
                }
                nextDayInSeconds += (24 * 60 * 60)
                if (dayWasEmpty) {
                    Log.i(
                        "loadUpcomingMatches()",
                        "nextDayInSeconds : $nextDayInSeconds. Current day was empty"
                    )
                } else {
                    Log.i(
                        "loadUpcomingMatches()",
                        "nextDayInSeconds : $nextDayInSeconds. Current day was not empty"
                    )
                }
                maxNumberOfDaysToLoad--
            } while (upcomingMatches.events.isEmpty() || dayWasEmpty || maxNumberOfDaysToLoad > 0)

            _loadingState.value = false
        }
    }

    fun loadData() {
        if (dataLoaded) {
            return
        }

        dataLoaded = true
        viewModelScope.launch(Dispatchers.IO) {
            val liveMatches = getLiveMatches()
            if (liveMatches.events.isNotEmpty()) {
                for ((index, event) in liveMatches.events.withIndex()) {
                    liveMatchesValues.add(event)
                    homeTeamIcons[index] = (getTeamImage(event.homeTeam.id))
                    awayTeamIcons[index] = (getTeamImage(event.awayTeam.id))
                }
            } else {
                Log.w(this.toString(), "There were no live matches?")
            }
            loadUpcomingMatches()
        }
    }
}