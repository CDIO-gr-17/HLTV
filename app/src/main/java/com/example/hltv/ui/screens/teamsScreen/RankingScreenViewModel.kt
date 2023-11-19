package com.example.hltv.ui.screens.teamsScreen

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.hltv.data.remote.APIResponse
import com.example.hltv.data.remote.getLiveMatches
import com.example.hltv.data.remote.getPlayerImage
import com.example.hltv.data.remote.getPlayersFromEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class TeamPlayerImages(
    val teamImages: MutableList<Bitmap>?,
    val teamID: Int?,
    val teamName: String?
)
data class AllPlayerImages(
    var allTeamImages: MutableList<TeamPlayerImages>?
)
suspend fun getPlayerGroups(eventID : Int?): Deferred<APIResponse.Lineup> = coroutineScope{
    return@coroutineScope async {
        Log.i("getPlayerGroups", "Getting player groups")
        return@async getPlayersFromEvent(eventID)
    }
}

suspend fun getAllPlayerImages(eventsWrapper: APIResponse.EventsWrapper): AllPlayerImages{
    Log.i("getAllPlayerImages", "Getting All player images")
    var allPlayerImages = AllPlayerImages(null)

    for (event in eventsWrapper.events){ //For every event

        var playerGroups = getPlayerGroups(event.id)
        val teamPlayerImages = TeamPlayerImages(null, event.id, event.homeTeam.name)
        for (player in playerGroups.await().home?.players!!){ //For every home player in that event
            CoroutineScope(Dispatchers.IO).launch {
                teamPlayerImages.teamImages?.add(getPlayerImage(player.player?.id))
            }
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
        CoroutineScope(Dispatchers.IO).launch{
            _playerImage.value = img(
                getPlayerImage())

        }

        CoroutineScope(Dispatchers.IO).launch {

            val liveMatches = getLiveMatches()
            CoroutineScope(Dispatchers.IO).launch {

            }
            teamNames.clear()
            if (liveMatches != null && liveMatches.events!=null) { //Despite what Android studio says, this seems to make a difference

                //TODO: This loop is called multiple times, i think. Pretty painful
                //Whole initializer is called multiple times, but stopping that breaks it
                for ((index, event) in liveMatches.events.withIndex()) {
                    Log.i("RankingScreen","Adding string with event " + index.toString() + ". Name is: " + event.homeTeam.name + " VS " + event.awayTeam.name)
                    teamNames.add(event.homeTeam.name + " VS " + event.awayTeam.name)
                }
                //I dont think this should be called here, but it is going to wait for getLiveMatches() anyway
                _allPlayerImages.value = getAllPlayerImages(liveMatches)

            }else{
                teamNames.add("No current teams playing")
                Log.i(this.toString(),"There were no live matches?")
            }
        }
    }
}