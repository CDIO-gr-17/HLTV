package com.example.hltv.ui.screens.singleTeam

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.painter.Painter
import androidx.lifecycle.ViewModel
import com.example.hltv.data.remote.APIResponse
import com.example.hltv.data.remote.Country
import com.example.hltv.data.remote.PlayerGroup
import com.example.hltv.data.remote.Player_orsub
import com.example.hltv.data.remote.Score
import com.example.hltv.data.remote.Team
import com.example.hltv.data.remote.getPlayerImage
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
    val image: Bitmap ?= null,
    val playerId: Int ?= null,
)
class SingleTeamViewModel: ViewModel() {
    val teamID = 364378
    val recentMatches = mutableStateListOf<RecentMatch>()
    val playerOverview = mutableStateListOf<Player>()
    lateinit var team1 : Team
    lateinit var team2 : Team
    var team1score : Score ?= null
    var team2score : Score ?= null


    init {
        val completedMatchesDeferred = CompletableDeferred<APIResponse.EventsWrapper>()

        CoroutineScope(Dispatchers.IO).launch {
            completedMatchesDeferred.complete(getPreviousMatches(teamID, 0))
            Log.w(this.toString(), "Got previous matches of team with id: $teamID")
        }
        CoroutineScope(Dispatchers.IO).launch {
            var lineup = getPlayersFromEvent()
            val completedMatches = completedMatchesDeferred.await()
            playerOverview.clear()
            recentMatches.clear()
            for ((index, event) in completedMatches.events.reversed().withIndex().take(6)) {
                if(teamID == event.homeTeam.id){
                    team1 = event.homeTeam
                    team2 = event.awayTeam
                    team1score = event.homeScore
                    team2score = event.awayScore
                }
                if(teamID != event.homeTeam.id){
                    team1 = event.awayTeam
                    team2 = event.homeTeam
                    team1score = event.awayScore
                    team2score = event.homeScore
                }
                lineup = getPlayersFromEvent(event.id)
                val date = Date(event.startTimestamp?.toLong()?.times(1000) ?: 0)
                val dateFormat = SimpleDateFormat("dd MMM.")
                val formattedDate = dateFormat.format(date)
                val recentMatch = RecentMatch(
                    homeTeam = team1,
                    awayTeam = team2,
                    homeTeamImage = getTeamImage(team1.id),
                    awayTeamImage = getTeamImage(team2.id),
                    homeScore = team1score,
                    awayScore = team2score,
                    startTimestamp = formattedDate,
                    bestOf = event.bestOf,
                )
                recentMatches.add(recentMatch)
                Log.w(this.toString(), "Added recent match with homeTeam ${event.homeTeam.name}, ${event.homeScore}, ${event.bestOf}")
            }
            Log.w(this.toString(), "Loaded lineup ${lineup}" )
            if(lineup.home!=null) {
                for (playerorsub in lineup.home!!.players) {
                    val player = Player(
                        name = playerorsub.player?.name,
                        image = getPlayerImage(playerorsub.player?.id,),
                        playerId = playerorsub.player?.id,
                    )
                    playerOverview.add(player)
                }
            }
        }

    }
}
