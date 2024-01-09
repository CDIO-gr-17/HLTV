package com.example.hltv.ui.screens.eventsScreen

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hltv.data.remote.APIResponse
import com.example.hltv.data.remote.Season
import com.example.hltv.data.remote.ThirdUniqueTournament
import com.example.hltv.data.remote.UniqueTournamentInfo
import com.example.hltv.data.remote.getRelevantTournaments
import com.example.hltv.data.remote.getUniqueTournamentDetails
import com.example.hltv.data.remote.getUniqueTournamentSeasons
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class EventsScreenViewModel : ViewModel() {
    val tournaments = mutableStateListOf<ThirdUniqueTournament>(ThirdUniqueTournament("Loading Tournaments"))
    val tournamentSeasons = mutableStateListOf<APIResponse.SeasonsWrapper>()
    val uniqueTournamentInfo = mutableStateListOf<UniqueTournamentInfo>()
    //private var _tournaments = MutableStateFlow<ThirdUniqueTournament>(ThirdUniqueTournament())
    //var tournaments = _tournaments.asStateFlow()


    fun loadData(){
        viewModelScope.launch(Dispatchers.IO){
            val tournamentsList = getRelevantTournaments()
            tournaments.clear()
            for ((index, tournament) in tournamentsList.withIndex()) {
                val tournamentSeasons = getUniqueTournamentSeasons(tournament.id)
                val seasonID = tournamentSeasons.seasons[0].id
                val uniqueTournament = getUniqueTournamentDetails(tournament.id, seasonID)
                Log.i("uniqueTournament", "$uniqueTournament")
                uniqueTournamentInfo.add(uniqueTournament.uniqueTournamentInfo)
                tournaments.add(tournament)
                Log.i("uniqueSeasons", "${tournament.name}, ${tournamentSeasons.seasons[0].name}, Prizemoney: ${uniqueTournament.uniqueTournamentInfo.totalPrizeMoney}")
            }
        }
    }
}