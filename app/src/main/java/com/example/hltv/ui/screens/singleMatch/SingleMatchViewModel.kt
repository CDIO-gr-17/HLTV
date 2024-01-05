package com.example.hltv.ui.screens.singleMatch

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hltv.data.remote.Game
import com.example.hltv.data.remote.Prediction
import com.example.hltv.data.remote.getGamesFromEvent
import com.example.hltv.data.remote.getMapImageFromMapID
import com.example.hltv.data.remote.getPredictionFromFirestore
import com.example.hltv.data.remote.sendPredictionToFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SingleMatchViewModel(matchID: String?) : ViewModel() {
    var prediction: MutableState<Prediction> = mutableStateOf(Prediction(0, 0))
    val niceMatchID = matchID!!.removePrefix("{matchID}").toInt()
    val games = mutableListOf<Game>()
    val teamImages = MutableList<Bitmap?>(2){null}
    val mapImages = mutableListOf<Bitmap?> (null)

    fun loadGames() {
        viewModelScope.launch(Dispatchers.IO) {
            games.addAll(getGamesFromEvent(niceMatchID).games)
val tempGames = List<Game>(1,){Game()}
            if (tempGames != null) {
                val homeTeamImage = null
                val awayTeamImage = null
                if (homeTeamImage != null && awayTeamImage != null) {
                    teamImages.add(homeTeamImage)
                    teamImages.add(awayTeamImage)
                } else {
                    Log.d("SingleMatchViewModel", "Team images are null")
                }
                games.forEach { game ->                 //get map images
                    val mapImage = getMapImageFromMapID(game.map?.id!!)
                    if (mapImage != null) {
                        mapImages.add(mapImage)
                    } else {
                        Log.d("SingleMatchViewModel", "Map image is null")
                    }
                }

                games.addAll(tempGames)
            } else {
            }
            getPrediction()

    }

    }
    init {

    }

    fun getPrediction() {
        CoroutineScope(Dispatchers.IO).launch {
            val tempPrediction =
                getPredictionFromFirestore(niceMatchID)
            if (tempPrediction == null) {
                prediction.value = Prediction(0, 0)
                return@launch
            } else {
                prediction.value = tempPrediction
            }
            calculateVotePercentage(prediction.value)
        }
    }

    fun updatePrediction(vote: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            if (vote == 1) {
                prediction.value = Prediction(
                    prediction.value.homeTeamVoteCount + 1,
                    prediction.value.awayTeamVoteCount
                )
            } else if (vote == 2) {
                prediction.value = Prediction(
                    prediction.value.homeTeamVoteCount,
                    prediction.value.awayTeamVoteCount + 1
                )
            } else {
                return@launch
            }
            calculateVotePercentage(prediction.value)
            sendPredictionToFirestore(prediction.value, niceMatchID)
        }
    }
    fun calculateVotePercentage(prediction: Prediction) {
        val totalVotes = prediction.homeTeamVoteCount + prediction.awayTeamVoteCount
        if(totalVotes == 0){
            Log.d("SingleMatchViewModel", "totalVotes = 0")
            return
        }
        prediction.homeTeamVotePercentage =
            prediction.homeTeamVoteCount * 100 / totalVotes
        prediction.awayTeamVotePercentage =
            prediction.awayTeamVoteCount * 100 / totalVotes
    }
}