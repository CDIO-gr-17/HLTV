package com.example.hltv.ui.screens.eventsScreen

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.hltv.data.remote.ThirdUniqueTournament
import com.example.hltv.data.remote.getRelevantTournaments
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class EventsScreenViewModel : ViewModel() {
    val tournaments = mutableStateListOf<ThirdUniqueTournament>()
    //private var _tournaments = MutableStateFlow<ThirdUniqueTournament>(ThirdUniqueTournament())
    //var tournaments = _tournaments.asStateFlow()

    init {
        val tournamentsDefered = CompletableDeferred<List<ThirdUniqueTournament>>()
        CoroutineScope(Dispatchers.IO).launch {
            tournamentsDefered.complete(getRelevantTournaments())
        }
        CoroutineScope(Dispatchers.Default).launch {
            val tournamentsList = tournamentsDefered.await()
            for (tournament in tournamentsList) {
                tournaments.add(tournament)
            }
        }
    }

}