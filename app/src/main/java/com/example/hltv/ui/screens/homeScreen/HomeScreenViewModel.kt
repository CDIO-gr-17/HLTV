package com.example.hltv.ui.screens.homeScreen

import android.graphics.Bitmap
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hltv.data.convertTimestampToDateURL
import com.example.hltv.data.remote.Event
import com.example.hltv.data.remote.getLiveMatches
import com.example.hltv.data.remote.getMatchesFromDay
import com.example.hltv.data.remote.getTeamImage
import com.example.hltv.data.remote.getTournamentLogo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeScreenViewModel: ViewModel() {

    val liveMatchValue = mutableStateOf<Event?>(null)
    val upcomingMatchValue = mutableStateOf<Event?>(null)

    val awayTeamIcon = mutableStateOf<Bitmap?>(null)
    var homeTeamIcon = mutableStateOf<Bitmap?>(null)
    var tournamentIcon = mutableStateOf<Bitmap?>(null)

    private var dataLoaded = false

    fun loadData(){
        if (dataLoaded){
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
                val liveMatches = getLiveMatches()

                if (liveMatches.events.isNotEmpty()) {
                    val event = liveMatches.events[0]
                    liveMatchValue.value = event
                    homeTeamIcon.value = getTeamImage(event.homeTeam.id)
                    awayTeamIcon.value = getTeamImage(event.awayTeam.id)
                    tournamentIcon.value = getTournamentLogo(event.tournament.uniqueTournament?.id)
                }
                else {
                    loadUpcomingMatch()
                }
            dataLoaded = true
        }

    }

    private suspend fun loadUpcomingMatch() {
        val upcomingMatches = getMatchesFromDay(convertTimestampToDateURL((System.currentTimeMillis()/1000).toInt()))

        if(upcomingMatches.events.isNotEmpty()){
            upcomingMatches.events = upcomingMatches.events.sortedBy { it.startTimestamp }
            var goodEvent : Event? = null
            for (event in upcomingMatches.events) {
                if (event.startTimestamp?.toLong() != null &&  //Makes sure that the upcoming match has an associated startTimestamp
                    event.startTimestamp!! > (System.currentTimeMillis() / 1000)
                ) {
                    goodEvent = event
                    break
                }
            }
            upcomingMatchValue.value = goodEvent
            homeTeamIcon.value = getTeamImage(upcomingMatchValue.value?.homeTeam?.id)
            awayTeamIcon.value = getTeamImage(upcomingMatchValue.value?.awayTeam?.id)
            tournamentIcon.value = getTournamentLogo(upcomingMatchValue.value?.tournament?.uniqueTournament?.id)

            }
        }
    }