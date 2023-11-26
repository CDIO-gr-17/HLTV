package com.example.hltv.ui.screens.playerScreen

import android.graphics.Bitmap
import com.example.hltv.data.remote.Player
import com.example.hltv.data.remote.PlayerGroup
import com.example.hltv.data.remote.Team
import com.example.hltv.data.remote.getLiveMatches
import com.example.hltv.data.remote.getPlayerImage
import com.example.hltv.ui.screens.teamsScreen.img
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


data class SinglePlayerData(
    var playerImage: Bitmap? = null,
    var player : Player? = null,
    var team : Team? = null,

    //TODO: add stats?
)

class PlayerScreenViewModel(playerID: String){

    private val _singlePlayerData = MutableStateFlow<SinglePlayerData>(SinglePlayerData())
    val singlePlayerData = _singlePlayerData.asStateFlow()

    init{
        val deferredPlayer = CompletableDeferred<SinglePlayerData>()
        CoroutineScope(Dispatchers.IO).launch {
            //TODO: deferredPlayer.complete(getPlayerData())
            deferredPlayer.complete(SinglePlayerData(player = Player(name = "PlaceholderName", id = playerID.toInt())))
            _singlePlayerData.value.player = deferredPlayer.await().player;
        }

        CoroutineScope(Dispatchers.IO).launch {
            _singlePlayerData.value.playerImage = getPlayerImage(playerID.toInt())
        }







    }
}