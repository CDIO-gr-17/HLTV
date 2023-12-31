package com.example.hltv.ui.screens.singleTeamScreen

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.hltv.data.remote.APIResponse
import com.example.hltv.data.remote.Country
import com.example.hltv.data.remote.PlayerGroup
import com.example.hltv.data.remote.Score
import com.example.hltv.data.remote.Team
import com.example.hltv.data.remote.getPlayerImage
import com.example.hltv.data.remote.getPlayersFromEvent
import com.example.hltv.data.remote.getPreviousMatches
import com.example.hltv.data.remote.getTeamImage
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date


var called = false;

data class SingleTeam(
    val eventsWrapper: APIResponse.EventsWrapper
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
    var dateOfBirthTimestamp: Int ?= null
)
data class Stats(
    val country: Country ?= null,
    val avgAgeofPlayers : Double ?= null,
)
class SingleTeamViewModel(): ViewModel() {

    val recentMatches = mutableStateListOf<RecentMatch>()
    val playerOverview = mutableStateListOf<Player>()
    //var lineup: PlayerGroup? = null
    var statisticsOverview = mutableStateOf<Stats>(Stats())
    var team1: Team? = null
    var team2: Team? = null
    var team1score: Score? = null
    var team2score: Score? = null
    var avgAgeofPlayers: Long = 0
    var avgAgeofPlayersString = ""
    var playersWithAge : Int = 0
    var teamID = 0
    val playersDateOfBirthTimestamp = mutableStateListOf<Int>()
    fun loadData(teamIDString: String){
        val teamID = teamIDString.removePrefix("{teamID}").toInt()

        val lineup = CompletableDeferred<PlayerGroup?>()

        CoroutineScope(Dispatchers.IO).launch {
            Log.w(this.toString(), "Got previous matches of team with id: $teamID")
            val completedMatches = getPreviousMatches(teamID, 0)
            recentMatches.clear()
            //recentMatches
            team1 = null
            for ((index, event) in completedMatches.events.reversed().withIndex().take(6)) {
                if (teamID == event.homeTeam.id) {
                    team1 = event.homeTeam
                    team2 = event.awayTeam
                    team1score = event.homeScore
                    team2score = event.awayScore
                    if (index == 0){
                        Log.i("asdasd", "Also loading here")
                        lineup.complete(getPlayersFromEvent(event.id).home)
                    }

                }
                if (teamID != event.homeTeam.id) {
                    team1 = event.awayTeam
                    team2 = event.homeTeam
                    team1score = event.awayScore
                    team2score = event.homeScore
                    if (index == 0){
                        Log.i("asdasd", "Loading here")
                        lineup.complete(getPlayersFromEvent(event.id).away)
                    }

                }
                val date = Date(event.startTimestamp?.toLong()?.times(1000) ?: 0)
                val dateFormat = SimpleDateFormat("dd MMM.")
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
                )
                recentMatches.add(recentMatch)
                Log.w(
                    this.toString(),
                    "Added recent match with homeTeam ${event.homeTeam.name}, ${event.homeScore}, ${event.bestOf}"
                )
            }
        }
        CoroutineScope(Dispatchers.IO).launch {
            // Lineup
            Log.w(this.toString(), "Loaded lineup ${lineup}")
            if (lineup != null) {
                playerOverview.clear() //TODO: Idk if this clear is really needed
                for (playerorsub in lineup.await()!!.players) {
                    val player = Player(
                        name = playerorsub.player?.name,
                        image = getPlayerImage(playerorsub.player?.id,),
                        playerId = playerorsub.player?.id,
                    )
                    playerOverview.add(player)
                    if(playerorsub.player?.dateOfBirthTimestamp!=null) { // Checks if the player has a dateOfBirthTimeStimp
                        playersDateOfBirthTimestamp.add(playerorsub.player!!.dateOfBirthTimestamp!!)
                    }
                    else
                        Log.i("avgAgeOfPlayers", "Player ${player.name} had dateOfBirthTimeStamp = null. Left out of calculation" )
                }

                statisticsOverview.value = Stats(
                    avgAgeofPlayers = getAvgAgeFromTimestamp(playersDateOfBirthTimestamp),
                    country = team1?.country
                )
            }
        }
    }
    fun getAvgAgeFromTimestamp(dateOfBirthTimestampList: MutableList<Int>): Double {            //TODO: This should be moved to a more appropriate place
        var totalAgeOfPlayers: Long = 0
        for (dateOfBirthTimestamp in dateOfBirthTimestampList) {
            totalAgeOfPlayers += ((System.currentTimeMillis() // Subtracts the current time in milliseconds from the players date of birth in milliseconds
                    - (dateOfBirthTimestamp.toLong() * 1000)))
        }
        if(dateOfBirthTimestampList.size!=0) {
            val avgAgeOfPlayersInMillis: Long = totalAgeOfPlayers / dateOfBirthTimestampList.size
            val df = DecimalFormat("#.#")
            val avgAgeOfPlayersInYears = avgAgeOfPlayersInMillis/365.25/3600/24/1000
            df.roundingMode = RoundingMode.CEILING
            print(avgAgeOfPlayersInYears.toDouble())
            return avgAgeOfPlayersInYears.toDouble()

            // String.format("%.1f", TimeUnit.MILLISECONDS.toDays(avgAgeOfPlayersInMillis) / 365.25).toDouble()

        }
        else return 0.0
    }
}


