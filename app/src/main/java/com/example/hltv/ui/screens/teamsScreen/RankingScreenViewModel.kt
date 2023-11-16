package com.example.hltv.ui.screens.teamsScreen

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import com.example.hltv.data.remote.getLiveMatches
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.EnumSet.range

class RankingScreenViewModel: ViewModel() {
    val teamNames = mutableStateListOf(" ", " ", " ", " ", " ")

    init{
        CoroutineScope(Dispatchers.IO).launch {
            val liveMatches = getLiveMatches();
            teamNames.clear()
            if (liveMatches != null) {
                //Log.i("RankingScreen", "Size of liveMatches is: " + liveMatches.events.size.toString())

                for ((index, event) in liveMatches.events.withIndex()) {
                    Log.i("RankingScreen","Adding string with event" + index.toString() + ". Name is: " + event.homeTeam.name + " VS " + event.awayTeam.name)
                    teamNames.add(event.homeTeam.name + " VS " + event.awayTeam.name)
                }
            }


        }
    }
}