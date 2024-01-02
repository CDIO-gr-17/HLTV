package com.example.hltv.ui.screens.matchesScreen

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hltv.data.convertTimestampToDateURL
import com.example.hltv.data.remote.APIResponse
import com.example.hltv.data.remote.Event
import com.example.hltv.data.remote.getLiveMatches
import com.example.hltv.data.remote.getPlayerImage
import com.example.hltv.data.remote.getPlayersFromEvent
import com.example.hltv.data.remote.getTeamImage
import com.example.hltv.data.remote.getMatchesFromDay
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

data class TeamPlayerImages(
    val teamImages: MutableList<Bitmap?>?,
    val teamID: Int?,
    val teamName: String?
)
data class AllPlayerImages(
    var allTeamImages: MutableList<TeamPlayerImages>?
)

suspend fun getPlayerGroups(eventID : Int?): Deferred<APIResponse.Lineup> = coroutineScope{
    return@coroutineScope async {
        return@async getPlayersFromEvent(eventID)
    }
}

suspend fun getAllPlayerImages(eventsWrapper: APIResponse.EventsWrapper): AllPlayerImages{
    Log.d("getAllPlayerImages", "Getting All player images")
    var allPlayerImages = AllPlayerImages(mutableListOf())
    for (event in eventsWrapper.events){ //For every event
        var playerGroups = getPlayerGroups(event.id)
        val teamPlayerImages = TeamPlayerImages(mutableListOf(), event.id, event.homeTeam.name)
        for (player in playerGroups.await().home?.players!!){ //For every home player in that event
            //////////////////// SHOULD STRONGLY BE INSIDE COROUTINE? IDK because we are limited by api rate anyway
            val bitmap = getPlayerImage(player.player?.id)
            teamPlayerImages.teamImages?.add(bitmap)
            //////////////////
        }

        allPlayerImages.allTeamImages?.add(teamPlayerImages)
    }
    return allPlayerImages
}

data class img(
    var bitMap: Bitmap?
)

class MatchesScreenViewModel: ViewModel() {
    val liveMatchesValues = mutableStateListOf<Event>()
    val upcomingMatchesValues = mutableStateListOf<Event>()

    val awayTeamIcons = MutableList<Bitmap?>(999){null}
    val homeTeamIcons = MutableList<Bitmap?>(999){null}

    fun loadData(){
        viewModelScope.launch {
            CoroutineScope(Dispatchers.IO).launch {
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
                val upcomingMatches = getMatchesFromDay(convertTimestampToDateURL((System.currentTimeMillis()/1000).toInt()))
                upcomingMatches.events = upcomingMatches.events.sortedBy { it.startTimestamp }
                if(upcomingMatches.events.isNotEmpty()){
                    for ((index, event) in upcomingMatches.events.withIndex()) {
                        if(event.homeScore!!.current==null) { //Assumes that all upcoming matches have scores of null, and only displays these. Might be an issue.
                            upcomingMatchesValues.add(event)
                            homeTeamIcons[index] = (getTeamImage(event.homeTeam.id))
                            awayTeamIcons[index] = (getTeamImage(event.awayTeam.id))
                        }
                    }
                }
            }
        }
    }
}