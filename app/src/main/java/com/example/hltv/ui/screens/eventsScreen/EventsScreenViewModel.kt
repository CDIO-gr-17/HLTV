package com.example.hltv.ui.screens.eventsScreen

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hltv.data.remote.APIResponse
import com.example.hltv.data.remote.Season
import com.example.hltv.data.remote.ThirdUniqueTournament
import com.example.hltv.data.remote.UniqueTournamentInfo
import com.example.hltv.data.remote.getRelevantTournaments
import com.example.hltv.data.remote.getTournamentLogo
import com.example.hltv.data.remote.getUniqueTournamentDetails
import com.example.hltv.data.remote.getUniqueTournamentSeasons
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class EventsScreenViewModel : ViewModel() {
    val tournaments = mutableStateListOf(ThirdUniqueTournament("Loading Tournaments"))
    val uniqueTournamentInfo = mutableStateListOf<UniqueTournamentInfo>()
    var tournamentSeasons = mutableStateListOf<ArrayList<Season>>()
    var uniqueTournaments = mutableStateListOf<APIResponse.UniqueTournamentInfoWrapper>()
    val tournamentIcons = MutableList<Bitmap?>(999){null}


    fun loadData(){
        viewModelScope.launch(Dispatchers.IO){
            val tournamentsList = getRelevantTournaments()
            tournaments.clear()
            for ((index, tournament) in tournamentsList.withIndex()) {
                tournamentSeasons.add(getUniqueTournamentSeasons(tournament.id).seasons)
                val seasonID = tournamentSeasons[index][0].id // I think 0 is always the most recent season? Not sure tho
                uniqueTournaments.add(getUniqueTournamentDetails(tournament.id, seasonID))
                uniqueTournamentInfo.add(uniqueTournaments[index].uniqueTournamentInfo)
                tournaments.add(tournament)
                tournamentIcons[index] = getTournamentLogo(tournament.id)
            }
        }
    }
}