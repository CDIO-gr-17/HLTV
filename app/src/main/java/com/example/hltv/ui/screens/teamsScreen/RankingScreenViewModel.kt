package com.example.hltv.ui.screens.teamsScreen

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.hltv.data.remote.APIResponse
import com.example.hltv.data.remote.getLiveMatches
import com.example.hltv.data.remote.getPlayerImage
import com.example.hltv.data.remote.getPlayersFromEvent
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class TeamPlayerImages(
    val teamImages: MutableList<Bitmap?>?,
    val teamID: Int?,
    val teamName: String?
)
data class AllPlayerImages(
    var allTeamImages: MutableList<TeamPlayerImages>?
)
suspend fun getPlayerGroups(eventID : Int?): Deferred<APIResponse.Lineup> = coroutineScope{
    return@coroutineScope async {
        return@async getPlayersFromEvent(eventID)
    }
}

suspend fun getAllPlayerImages(eventsWrapper: APIResponse.EventsWrapper): AllPlayerImages{
    Log.d("getAllPlayerImages", "Getting All player images")
    var allPlayerImages = AllPlayerImages(mutableListOf())
    for (event in eventsWrapper.events){ //For every event
        var playerGroups = getPlayerGroups(event.id)
        val teamPlayerImages = TeamPlayerImages(mutableListOf(), event.id, event.homeTeam.name)
        for (player in playerGroups.await().home?.players!!){ //For every home player in that event
            //////////////////// SHOULD STRONGLY BE INSIDE COROUTINE? IDK because we are limited by api rate anyway
            val bitmap = getPlayerImage(player.player?.id)
            teamPlayerImages.teamImages?.add(bitmap)
            //////////////////
        }

        allPlayerImages.allTeamImages?.add(teamPlayerImages)
    }
    return allPlayerImages
}

data class img(
    val bitMap: Bitmap?
)

class RankingScreenViewModel: ViewModel() {
    val teamNames = mutableStateListOf("1", "2", "3", "4", "5")
    private var _allPlayerImages = MutableStateFlow<AllPlayerImages>(AllPlayerImages(null))
    var allPlayerImages = _allPlayerImages.asStateFlow()
    private val _playerImage = MutableStateFlow<img>(img(null))
    val playerImage = _playerImage.asStateFlow()

    init{

        var liveMatchesDeferred = CompletableDeferred<APIResponse.EventsWrapper>();

        CoroutineScope(Dispatchers.IO).launch {
            liveMatchesDeferred.complete(getLiveMatches())
        }
        CoroutineScope(Dispatchers.Default).launch {
            val liveMatches = liveMatchesDeferred.await()
            teamNames.clear()
            if (liveMatches != null && liveMatches.events!=null) { //Despite what Android studio says, this seems to make a difference
                for ((index, event) in liveMatches.events.withIndex()) {
                    //Log.i("RankingScreen","Adding string with event " + index.toString() + ". Name is: " + event.homeTeam.name + " VS " + event.awayTeam.name)
                    teamNames.add(event.homeTeam.name + " VS " + event.awayTeam.name)
                }
                //I dont think this should be called here, but it is going to wait for getLiveMatches() anyway
                _allPlayerImages.value = getAllPlayerImages(liveMatches)

            }else{
                teamNames.add("No current teams playing")
                Log.w(this.toString(),"There were no live matches?")
            }
        }



        CoroutineScope(Dispatchers.IO).launch {
            _allPlayerImages.value = getAllPlayerImages(liveMatchesDeferred.await())
        }
    }
}