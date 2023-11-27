package com.example.hltv.ui.screens.eventsScreen

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hltv.data.remote.ThirdUniqueTournament
import com.example.hltv.data.remote.getRelevantTournaments
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class EventsScreenViewModel : ViewModel() {
    val tournaments = mutableStateListOf<ThirdUniqueTournament>(ThirdUniqueTournament("Loading Tournaments"))
    //private var _tournaments = MutableStateFlow<ThirdUniqueTournament>(ThirdUniqueTournament())
    //var tournaments = _tournaments.asStateFlow()


    fun loadData(){
        viewModelScope.launch{
            CoroutineScope(Dispatchers.IO).launch {
                val tournamentsList = getRelevantTournaments()
                tournaments.clear()
                for (tournament in tournamentsList) {
                    tournaments.add(tournament)
                }
            }
        }
    }
}