package com.example.hltv.ui.screens.eventsScreen

import androidx.lifecycle.ViewModel
import com.example.hltv.data.remote.ThirdUniqueTournament
import com.example.hltv.data.remote.getRelevantTournaments
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch



class EventsScreenViewModel : ViewModel() {
    var tournaments : List<ThirdUniqueTournament> = emptyList()
    init {
        CoroutineScope(Dispatchers.IO).launch {
            tournaments = getRelevantTournaments()
            }
        }
    }