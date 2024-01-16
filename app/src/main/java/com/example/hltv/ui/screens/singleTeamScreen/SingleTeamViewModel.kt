package com.example.hltv.ui.screens.singleTeamScreen

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.example.hltv.data.getAvgAgeFromTimestamp
import com.example.hltv.data.remote.APIResponse
import com.example.hltv.data.remote.PlayerGroup
import com.example.hltv.data.remote.Score
import com.example.hltv.data.remote.Team
import com.example.hltv.data.remote.getPlayerImage
import com.example.hltv.data.remote.getPlayersFromEvent
import com.example.hltv.data.remote.getPreviousMatches
import com.example.hltv.data.remote.getTeamImage
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

data class RecentMatch(
    val homeTeam: Team? = null,
    val awayTeam: Team? = null,
    val homeTeamImage: Bitmap? = null,
    val awayTeamImage: Bitmap? = null,
    val homeScore: Score? = null,
    val awayScore: Score? = null,
    val startTimestamp: String? = null,
    val bestOf: Int? = null,
    val matchID: Int? = null
)
data class Player(
    val name: String ?= null,
    val image: Bitmap ?= null,
    val playerId: Int ?= null,
    var dateOfBirthTimestamp: Int ?= null
)
data class Stats(
    val countryName: String ?= null,
    val countryCode: String ?= null,
    val avgAgeofPlayers : Double ?= null,
    val winRate : String ?= null,
)
class SingleTeamViewModel : ViewModel() {

    val recentMatches = mutableStateListOf<RecentMatch>()
    val playerOverview = mutableStateListOf<Player>()
    var statisticsOverview = mutableStateOf(Stats())
    val teamImage = mutableStateOf<Bitmap?>(null)
    private var team1: Team? = null
    private var team2: Team? = null
    val team = mutableStateOf(Team())
    var team1score: Score? = null
    var team2score: Score? = null
    var teamID = 0
    val playersDateOfBirthTimestamp = mutableStateListOf<Int>()
    val color = mutableStateOf(Color.White)
    var winRate = MutableStateFlow(0.0)
    val noInfoOnTeam = mutableStateOf(false)

    var dataLoaded = false
    fun loadData(teamIDString: String, gamesToLoad: Int = 6) {

        //I love how jank this is but it works, I think. Loading a new team loads a new viewmodel
        //where dataloaded will default to false, so I think it works?
        if (dataLoaded) {
            return
        }
        dataLoaded = true
        clearData()



        val teamID = teamIDString.toInt()
        val lineup = CompletableDeferred<PlayerGroup?>()

        viewModelScope.launch(Dispatchers.IO) {

            teamImage.value = getTeamImage(teamID)
            //palette.value =  Palette.from(teamImage.value!!).generate()
            //color.value = Color(Palette.from(teamImage.value!!).generate().vibrantSwatch!!.rgb)

            if(teamImage.value != null) {
                val palette = Palette.from(teamImage.value!!).generate()
                if (teamImage.value != null && palette.vibrantSwatch?.rgb != null) {
                    color.value = Color(palette.vibrantSwatch?.rgb!!)
                } else color.value = Color.White
            } else color.value = Color.White

            Log.w(this.toString(), "Got previous matches of team with id: $teamID")
            var completedMatches : APIResponse.EventsWrapper
            try{
                completedMatches = getPreviousMatches(teamID, 0)
            } catch (e : IOException){
                Log.w("getPreviousMatches", "Tried loading matches for team with no previous matches")
                completedMatches = APIResponse.EventsWrapper(emptyList())
                noInfoOnTeam.value = true
            } catch (e : java.lang.ClassCastException){
                Log.w("getPreviousMatches", "Tried loading matches for team with no previous matches")
                completedMatches = APIResponse.EventsWrapper(emptyList())
                noInfoOnTeam.value = true
            }

            val filteredMatches = completedMatches.events
                .filter { it.status?.type == "finished" }
                .filter { it.homeScore?.current != null || it.awayScore?.current != null && it.homeScore?.current != 0 && it.awayScore?.current != 0 }
            recentMatches.clear()
            //recentMatches
            team1 = null
            var totalMatches = 0
            var totalWins = 0
            var lineupIncomplete = true
            for ((index, event) in filteredMatches.reversed().withIndex().take(gamesToLoad)) {
                if (teamID == event.homeTeam.id) {
                    team1 = event.homeTeam
                    team2 = event.awayTeam
                    team1score = event.homeScore
                    team2score = event.awayScore
                    if (lineupIncomplete) {
                        team.value = event.homeTeam //TODO: Maybe? move this into the try, after the other thing
                        try{
                            lineup.complete(getPlayersFromEvent(event.id).home)
                            lineupIncomplete = false
                        } catch (e : ClassCastException){
                            Log.w("SingleTeamViewModel", "There was no lineup available, attempting to get lineup from next event")
                        }

                    }
                }
                if (teamID != event.homeTeam.id) {
                    team1 = event.awayTeam
                    team2 = event.homeTeam
                    team1score = event.awayScore
                    team2score = event.homeScore
                    if (lineupIncomplete) {
                        team.value = event.awayTeam
                        try{
                            lineup.complete(getPlayersFromEvent(event.id).away)
                            lineupIncomplete = false
                        } catch (e : ClassCastException){
                            Log.w("SingleTeamViewModel", "There was no lineup available, attempting to get lineup from next event")
                        }
                    }
                }
                val date = Date(event.startTimestamp?.toLong()?.times(1000) ?: 0)
                val dateFormat = SimpleDateFormat("dd MMM.") //TODO: Fix this
                val formattedDate = dateFormat.format(date)
                val recentMatch = RecentMatch(
                    homeTeam = team1,
                    awayTeam = team2,
                    homeTeamImage = getTeamImage(team1?.id),
                    awayTeamImage = getTeamImage(team2?.id),
                    homeScore = team1score,
                    awayScore = team2score,
                    startTimestamp = formattedDate,
                    bestOf = event.bestOf,
                    matchID = event.id
                )
                if (team1score?.current!! > team2score?.current!!) {
                    totalWins++
                }
                totalMatches++
                recentMatches.add(recentMatch)
                Log.w(
                    this.toString(),
                    "Added recent match with homeTeam ${event.homeTeam.name}, ${event.homeScore}, ${event.bestOf}"
                )
            }
            Log.i("winRate","Totalmatches $totalMatches, totalWins $totalWins")
            winRate.value = if (totalMatches > 0.0) {
                (totalWins.toDouble()/ totalMatches) * 100
            } else {
                0.0
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            // Lineup
            Log.w(this.toString(), "Loaded lineup ${lineup}")
            if (lineup != null) {
                playerOverview.clear() //TODO: Idk if this clear is really needed
                for (playerorsub in lineup.await()!!.players) {
                    val player = Player(
                        name = playerorsub.player?.name,
                        image = getPlayerImage(playerorsub.player?.id),
                        playerId = playerorsub.player?.id,
                    )
                    playerOverview.add(player)
                    if (playerorsub.player?.dateOfBirthTimestamp != null) { // Checks if the player has a dateOfBirthTimeStimp
                        playersDateOfBirthTimestamp.add(playerorsub.player!!.dateOfBirthTimestamp!!)
                    } else
                        Log.i(
                            "avgAgeOfPlayers",
                            "Player ${player.name} had dateOfBirthTimeStamp = null. Left out of calculation"
                        )

                }
                statisticsOverview.value = Stats(
                    avgAgeofPlayers = getAvgAgeFromTimestamp(playersDateOfBirthTimestamp),
                    countryName = team1?.country?.name,
                    countryCode = team1?.country?.alpha2,
                )
            }
        }
    }
    private fun clearData() {
        recentMatches.clear()
        playerOverview.clear()
        statisticsOverview.value = Stats()
        teamImage.value = null
        team1 = null
        team2 = null
        team.value = Team()
        team1score = null
        team2score = null
        teamID = 0
        playersDateOfBirthTimestamp.clear()
        color.value = Color.White
        winRate.value = 0.0
        noInfoOnTeam.value = false
    }
}


