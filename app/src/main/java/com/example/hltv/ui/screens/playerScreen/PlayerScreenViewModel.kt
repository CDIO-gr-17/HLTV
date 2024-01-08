package com.example.hltv.ui.screens.playerScreen

import android.graphics.Bitmap
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


/*data class SinglePlayerData(
    var playerImage: Bitmap? = null,
    var player : Player? = null,
    var team : Team? = null,

    //TODO: add stats?
)
*/

class PlayerScreenViewModel():ViewModel() {

    var player = mutableStateOf<Player?>(null)
    var playerImage = mutableStateOf<Bitmap?>(null)

    fun loadData(playerIDfullString: String?) {
        val playerID = playerIDfullString!!.removePrefix("{playerID}").toInt() //TODO fix denne her basse
        viewModelScope.launch {
            CoroutineScope(Dispatchers.IO).launch {
                val data = getPlayerFromPlayerID(playerID)
                val image = getPlayerImage(playerID)
                player.value = data.player
                playerImage.value = image
            }
        }
    }
}









    /*private val _singlePlayerData = MutableStateFlow<SinglePlayerData>(SinglePlayerData())
    val singlePlayerData = _singlePlayerData.asStateFlow()


    init{
        val deferredPlayer = CompletableDeferred<Player>()
        CoroutineScope(Dispatchers.IO).launch {
            //deferredPlayer.complete(getPlayer("PlaceholderID"))
        }
        CoroutineScope(Dispatchers.IO).launch {
            _singlePlayerData.value.playerImage = getPlayerImage(deferredPlayer.await().id)
        }

*/






