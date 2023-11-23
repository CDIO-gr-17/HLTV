package com.example.hltv.ui.screens.singleTeam

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.painter.Painter
import androidx.lifecycle.ViewModel
import com.example.hltv.data.remote.APIResponse
import com.example.hltv.data.remote.Country
import com.example.hltv.data.remote.Score
import com.example.hltv.data.remote.Team
import com.example.hltv.data.remote.getPlayersFromEvent
import com.example.hltv.data.remote.getPreviousMatches
import com.example.hltv.data.remote.getTeamImage
import com.example.hltv.ui.screens.teamsScreen.getPlayerGroups
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date

data class SingleTeam(
    val eventsWrapper: APIResponse.EventsWrapper,
    val country: Country ?= null,
    val dateOfBirthTimestamp : Int ?= null,
    )
data class RecentMatch(
    val homeTeam: Team? = null,
    val awayTeam: Team? = null,
    val homeTeamImage: Bitmap? = null,
    val awayTeamImage: Bitmap? = null,
    val homeScore: Score? = null,
    val awayScore: Score? = null,
    val startTimestamp: String? = null,
    val bestOf: Int? = null,
)
data class Player(
    val name: String ?= null,
    val image: Painter,
    val playerId: Int ?= null,
)
data class PlayerOverview(
    val players : ArrayList<Player> ?= null,
)
class SingleTeamViewModel: ViewModel() {
    val teamID = 363904
    val recentMatches = mutableStateListOf<RecentMatch>()
    val playerOverview = mutableStateListOf<PlayerOverview>()

    init {

        //recentMatchesViewModel
        val completedMatchesDeferred = CompletableDeferred<APIResponse.EventsWrapper>()

        CoroutineScope(Dispatchers.IO).launch {
            completedMatchesDeferred.complete(getPreviousMatches(teamID, 0))
            Log.w(this.toString(), "Got previous matches of team with id: $teamID")
        }
        CoroutineScope(Dispatchers.IO).launch {
            val completedMatches = completedMatchesDeferred.await()
            playerOverview.clear()
            recentMatches.clear()
            for ((index, event) in completedMatches.events.reversed().withIndex().take(6)) {
                val lineup = getPlayersFromEvent(event.id).home
                val date = Date(event.startTimestamp?.toLong()?.times(1000) ?: 0)
                val dateFormat = SimpleDateFormat("dd MMM.")
                val formattedDate = dateFormat.format(date)
                val recentMatch = RecentMatch(
                    homeTeam = event.homeTeam,
                    awayTeam = event.awayTeam,
                    homeTeamImage = getTeamImage(event.homeTeam.id),
                    awayTeamImage = getTeamImage(event.awayTeam.id),
                    homeScore = event.homeScore,
                    awayScore = event.awayScore,
                    startTimestamp = formattedDate,
                    bestOf = event.bestOf,
                )
                recentMatches.add(recentMatch)
                Log.w(this.toString(), "Added recent match with homeTeam ${event.homeTeam.name}, ${event.homeScore}, ${event.bestOf}")
            }

        }

    }
}
