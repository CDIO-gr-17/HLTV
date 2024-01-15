package com.example.hltv.ui.screens.homeScreen

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hltv.data.convertTimestampToDateURL
import com.example.hltv.data.remote.APIResponse
import com.example.hltv.data.remote.Event
import com.example.hltv.data.remote.Score
import com.example.hltv.data.remote.Season
import com.example.hltv.data.remote.Team
import com.example.hltv.data.remote.ThirdUniqueTournament
import com.example.hltv.data.remote.UniqueTournamentInfo
import com.example.hltv.data.remote.getLiveMatches
import com.example.hltv.data.remote.getMatchesFromDay
import com.example.hltv.data.remote.getRelevantTournaments
import com.example.hltv.data.remote.getTeamImage
import com.example.hltv.data.remote.getTournamentLogo
import com.example.hltv.data.remote.getUniqueTournamentDetails
import com.example.hltv.data.remote.getUniqueTournamentSeasons
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeScreenViewModel: ViewModel() {

    val liveMatchValue = mutableStateOf<Event?>(null)
    val upcomingMatchValue = mutableStateOf<Event?>(null)

    val upcomingTournament = mutableStateOf<ThirdUniqueTournament?>(null)
    val uniqueTournament = mutableStateOf<APIResponse.UniqueTournamentInfoWrapper?>(null)
    val upcomingTournamentlogo = mutableStateOf<Bitmap?>(null)
    val tournamentSeason = mutableStateOf<Season?>(null)


    val upcomingMatchesValue = mutableStateOf<Event>(Event())

    val awayTeamIcon = mutableStateOf<Bitmap?>(null)
    var homeTeamIcon = mutableStateOf<Bitmap?>(null)

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
                }
                else {
                    loadUpcomingMatch()
                }
            viewModelScope.launch (Dispatchers.IO){
                loadUpcomingTournament()
            }

            dataLoaded = true
        }

    }

    private suspend fun loadUpcomingTournament(){
        val tournaments = getRelevantTournaments()
        if (tournaments.isNotEmpty())
        {
            Log.i("TournamentInfo", "Tournament  0 ID: "+ tournaments[0].id)
            val tournament = tournaments[1] // Should be remade so it doesnt take number 2 but something else
            upcomingTournament.value = tournament //gets the specific tournament data

            Log.i("TournamentInfo","Tournament name"+ upcomingTournament.value!!.name)
            upcomingTournamentlogo.value = getTournamentLogo(upcomingTournament.value!!.id) // gets the logo for the same tournament
            Log.i("TournamentInfo","Logo   "+ upcomingTournament.value!!.id)

            val seasonID =  getUniqueTournamentSeasons(tournament.id).seasons[0].id

            //val seasonID = tournamentSeason.value!!.id // Gets a Season ID
            Log.i("TournamentInfo","Season ID  "+ seasonID)
            uniqueTournament.value = getUniqueTournamentDetails(tournament.id, seasonID)
        }

    }

    private suspend fun loadUpcomingMatch() {
        val upcomingMatches = getMatchesFromDay(convertTimestampToDateURL((System.currentTimeMillis()/1000).toInt()))

        if(upcomingMatches.events.isNotEmpty()){
            upcomingMatches.events = upcomingMatches.events.sortedBy { it.startTimestamp }
            var goodevent : Event? = null
            for (event in upcomingMatches.events) {
                if (event.startTimestamp?.toLong() != null &&  //Makes sure that the upcoming match has an associated startTimestamp
                    event.startTimestamp!! > (System.currentTimeMillis() / 1000)
                ) {
                    goodevent = event
                    break
                }
            }
            upcomingMatchValue.value = goodevent
            homeTeamIcon.value = getTeamImage(upcomingMatchValue.value?.homeTeam?.id)
            awayTeamIcon.value = getTeamImage(upcomingMatchValue.value?.awayTeam?.id)

            }
        }
    }