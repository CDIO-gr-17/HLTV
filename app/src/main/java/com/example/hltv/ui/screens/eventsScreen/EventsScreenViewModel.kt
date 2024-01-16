package com.example.hltv.ui.screens.eventsScreen

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hltv.data.convertYearToUnixTimestamp
import com.example.hltv.data.remote.APIResponse
import com.example.hltv.data.remote.Season
import com.example.hltv.data.remote.ThirdUniqueTournament
import com.example.hltv.data.remote.UniqueTournamentInfo
import com.example.hltv.data.remote.getRelevantTournaments
import com.example.hltv.data.remote.getTournamentLogo
import com.example.hltv.data.remote.getUniqueTournamentDetails
import com.example.hltv.data.remote.getUniqueTournamentSeasons
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.lang.NumberFormatException

class EventsScreenViewModel : ViewModel() {
    var tournaments = mutableStateListOf(ThirdUniqueTournament("Loading Tournaments"))
    val uniqueTournamentInfo = mutableStateListOf<UniqueTournamentInfo>()
    var tournamentSeasons = mutableStateListOf<ArrayList<Season>>()
    var uniqueTournaments = mutableStateListOf<APIResponse.UniqueTournamentInfoWrapper>()
    val tournamentIcons = MutableList<Bitmap?>(999){null}
    var loading = mutableStateOf(false)

    private val _loadingState = MutableStateFlow(true)
    private var isLoaded = false
    val loadingState: StateFlow<Boolean> get() = _loadingState

    private var job : Job? = null


    fun cancelJob(){
         job?.cancel()
    }

    fun loadData(){
        if (isLoaded) return
        isLoaded = true

        job = viewModelScope.launch(Dispatchers.IO){


            var tournamentsList = getRelevantTournaments()
            tournaments.clear()
            for ((index, tournament) in tournamentsList.withIndex()) {
                tournamentSeasons.add(getUniqueTournamentSeasons(tournament.id).seasons)
                val seasonID = tournamentSeasons[index][0].id // I think 0 is always the most recent season? Not sure tho
                uniqueTournaments.add(getUniqueTournamentDetails(tournament.id, seasonID))
                uniqueTournamentInfo.add(uniqueTournaments[index].uniqueTournamentInfo)
                tournaments.add(tournament)
                tournamentIcons[index] = getTournamentLogo(tournament.id)
                if (tournament.startDateTimestamp == null){
                    try{
                        Log.i("EventsScreenViewModel",tournament.name + tournamentSeasons[index][0].name)
                        tournament.startDateTimestamp = convertYearToUnixTimestamp(tournament.name + tournamentSeasons[index][0].name)
                    } catch (e: NumberFormatException){
                        Log.i("EventsScreenViewModel","This exception is expected")
                    }
                }
                Log.i("EventsScreenViewModel", "Start date: " + tournament.startDateTimestamp)
            }
            _loadingState.value = false
        }
    }
}