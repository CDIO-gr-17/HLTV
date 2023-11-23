package com.example.hltv.ui.screens.singleTeam

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.hltv.data.remote.APIResponse
import com.example.hltv.data.remote.Country
import com.example.hltv.data.remote.PlayerGroup
import com.example.hltv.data.remote.Player_orsub
import com.example.hltv.data.remote.Score
import com.example.hltv.data.remote.Team
import com.example.hltv.data.remote.Time
import com.example.hltv.data.remote.getLiveMatches
import com.example.hltv.data.remote.getPreviousMatches
import com.example.hltv.data.remote.getTeamImage
import com.example.hltv.ui.screens.teamsScreen.getAllPlayerImages
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SingleTeam(
    val eventsWrapper: APIResponse.EventsWrapper,
    val players : ArrayList<Player> ?= null,
    val playerImage: Bitmap?= null,
    val country: Country ?= null,
    val dateOfBirthTimestamp : Int ?= null,
    )
data class RecentMatch(
    val homeTeam: Team ?= null,
    val awayTeam: Team ?= null,
    val homeTeamImage: Bitmap ?= null,
    val awayTeamImage: Bitmap ?= null,
    val homeScore: Score?= null,
    val awayScore: Score ?= null,
    val time: Time ?= null,
    val bestOf: Int ?= null,
)
class SingleTeamViewModel: ViewModel() {
    val recentMatches = mutableStateListOf<RecentMatch>()
    val team = 364425

    init {
        var completedMatchesDeferred = CompletableDeferred<APIResponse.EventsWrapper>();

        CoroutineScope(Dispatchers.IO).launch {
            completedMatchesDeferred.complete(getPreviousMatches(team))
            Log.w(this.toString(), "Got previous matches of team with id: $team")
        }
        CoroutineScope(Dispatchers.IO).launch {
            val completedMatches = completedMatchesDeferred.await()
            recentMatches.clear()
            if(recentMatches != null) {
                for ((index, event) in completedMatches.events.withIndex().take(6)) {
                    val recentMatch = RecentMatch(
                        homeTeam = event.homeTeam,
                        awayTeam = event.awayTeam,
                        homeTeamImage = getTeamImage(event.homeTeam.id),
                        awayTeamImage = getTeamImage(event.awayTeam.id),
                        homeScore = event.homeScore,
                        awayScore = event.awayScore,
                        time = event.time,
                        bestOf = event.bestOf,
                    )
                    recentMatches.add(recentMatch)
                    Log.w(this.toString(), "Added recent match with homeTeam ${event.homeTeam.name}, ${event.homeScore}, ${event.bestOf}")
                }
            }
            else{
                val recentMatch = RecentMatch(
                    bestOf = 0
                )
                recentMatches.add(recentMatch)
                Log.w(this.toString(),"There were no recent matches")
            }

        }

    }
}