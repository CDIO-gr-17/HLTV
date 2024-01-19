package com.example.hltv.ui.screens.playerScreen

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hltv.data.remote.Player
import com.example.hltv.data.remote.getPlayerFromPlayerID
import com.example.hltv.data.remote.getPlayerImage
import com.example.hltv.data.remote.getTeamImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class PlayerScreenViewModel :ViewModel() {

    var player = mutableStateOf<Player?>(null)
    var playerImage = mutableStateOf<Bitmap?>(null)
    var teamImage = mutableStateOf<Bitmap?>(null)

    fun loadData(playerIDFullString: String?) {
        Log.i("PlayerScreenViewModel", "playerIDFullString is: $playerIDFullString")
        val playerID = playerIDFullString?.toInt()
        viewModelScope.launch(Dispatchers.IO) {
            player.value = getPlayerFromPlayerID(playerID).player
            playerImage.value = getPlayerImage(playerID)
            teamImage.value = getTeamImage(player.value?.team?.id)
        }
    }
}






