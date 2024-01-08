package com.example.hltv.ui.screens.matchesScreen

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hltv.data.convertTimestampToDateURL
import com.example.hltv.data.remote.APIResponse
import com.example.hltv.data.remote.Event
import com.example.hltv.data.remote.getLiveMatches
import com.example.hltv.data.remote.getMatchesFromDay
import com.example.hltv.data.remote.getPlayerImage
import com.example.hltv.data.remote.getPlayersFromEvent
import com.example.hltv.data.remote.getTeamImage
import com.example.hltv.data.remote.getMatchesFromDay
import com.example.hltv.data.remote.getTournamentLogo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
    val tournamentValues = mutableStateListOf<Event>()

    var upcomingMatchIndex = 0
    var tournamentIndex = 0

    val awayTeamIcons = MutableList<Bitmap?>(999){null}
    val homeTeamIcons = MutableList<Bitmap?>(999){null}
    val tournamentIcons = MutableList<Bitmap?>(999){null}

    var nextDayInSeconds = System.currentTimeMillis()/1000

    private val _loadingState = MutableStateFlow(true)
    val loadingState: StateFlow<Boolean> get() = _loadingState

    private var dataLoaded = false
    suspend fun loadUpcomingMatches(){

            _loadingState.value = true
            Log.i("loadUpcomingMatches", "Loading state set to true")
            viewModelScope.launch {
                CoroutineScope(Dispatchers.IO).launch {
                    var dayWasEmpty = false
                    var MAXNUMBEROFDAYSTOLOAD = 15 //Prevent infinite loading in case theres no more tournaments, jamming the API
                    do{
                        val upcomingMatches = getMatchesFromDay(convertTimestampToDateURL((nextDayInSeconds).toInt()))

                        dayWasEmpty = true
                        if(upcomingMatches.events.isNotEmpty()){
                            upcomingMatches.events = upcomingMatches.events.sortedBy { it.startTimestamp }
                            for (event in upcomingMatches.events) {
                                if (event.startTimestamp?.toLong() != null &&  //Makes sure that the upcoming match has an associated startTimestamp
                                    event.startTimestamp!! > (System.currentTimeMillis() / 1000) && //Excludes matches where the startTimestamp has passed (i.e it is live or has been played)
                                    event !in upcomingMatchesValues) { //Questionable, but seems to work
                                    upcomingMatchesValues.add(upcomingMatchIndex, event)
                                    tournamentValues.add(tournamentIndex,event)
                                    homeTeamIcons[liveMatchesValues.size + upcomingMatchIndex] = (getTeamImage(event.homeTeam.id))
                                    awayTeamIcons[liveMatchesValues.size + upcomingMatchIndex] = (getTeamImage(event.awayTeam.id))
                                    tournamentIcons[tournamentIndex] = getTournamentLogo(event.tournament?.uniqueTournament?.id)
                                    //Log.i("Compare","${awayTeamIcons[liveMatchesValues.size + upcomingMatchIndex]}")
                                    //Log.i("tournamentLogo2", "Added tournamentLogo from tournament ${event.tournament?.name}, ${event.tournament?.uniqueTournament?.name} to ${tournamentIcons[tournamentIndex]} at $tournamentIndex")
                                    upcomingMatchIndex++
                                    tournamentIndex++
                                    dayWasEmpty = false
                                }
                            }
                        }
                        nextDayInSeconds += (24 * 60 * 60)
                        if (dayWasEmpty){
                            Log.i("loadUpcomingMatches()","nextdayinsceonds : $nextDayInSeconds. Current day was empty")
                        }
                        else{
                            Log.i("loadUpcomingMatches()","nextdayinsceonds : $nextDayInSeconds. Current day was not empty")
                        }
                        MAXNUMBEROFDAYSTOLOAD--
                    } while(upcomingMatches.events.isEmpty() || dayWasEmpty || MAXNUMBEROFDAYSTOLOAD>0)

                    _loadingState.value = false
                }
            }
    }
    fun loadData(){
        if (dataLoaded){
            return
        }

        dataLoaded = true
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
                loadUpcomingMatches()
            }
        }
    }
}