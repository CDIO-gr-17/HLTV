package com.example.hltv.ui.screens.playerScreen

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hltv.data.remote.APIResponse
import com.example.hltv.data.remote.Player
import com.example.hltv.data.remote.Team
import com.example.hltv.data.remote.ThirdUniqueTournament
import com.example.hltv.data.remote.getPlayerFromPlayerID
import com.example.hltv.data.remote.getPlayerImage
import com.example.hltv.data.remote.getTeamImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch



class PlayerScreenViewModel():ViewModel() {

    var player = mutableStateOf<Player?>(null)
    var playerImage = mutableStateOf<Bitmap?>(null)
    var teamImage = mutableStateOf<Bitmap?>(null)

    fun loadData(playerIDfullString: String?) {
        Log.i("PlayerScreenViewModel", "playerIDfullString is: $playerIDfullString")
        val playerID = playerIDfullString?.toInt()
        viewModelScope.launch(Dispatchers.IO) {
            player.value = getPlayerFromPlayerID(playerID).player
            playerImage.value = getPlayerImage(playerID)
            teamImage.value = getTeamImage(player.value?.team?.id)
        }
    }
}






