package com.example.hltv.ui.screens.singleMatch

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.hltv.data.remote.Prediction
import com.example.hltv.data.remote.getPredictionFromFirestore
import com.example.hltv.data.remote.sendPredictionToFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SingleMatchViewModel(var matchID: String?) : ViewModel() {
    var prediction: MutableState<Prediction> = mutableStateOf(Prediction(0, 0))


    init {
        CoroutineScope(Dispatchers.IO).launch {
            getPrediction()


        }
    }

    fun getPrediction() {
        CoroutineScope(Dispatchers.IO).launch {
            val tempPrediction =
                getPredictionFromFirestore(matchID!!.removePrefix("{matchID}").toInt())
            if (tempPrediction == null) {
                prediction.value = Prediction(0, 0)
                return@launch
            } else {
                prediction.value = tempPrediction
            }
            val totalVotes = prediction.value.homeTeamVoteCount + prediction.value.awayTeamVoteCount
            if (totalVotes != 0) {
                prediction.value.homeTeamVotePercentage =
                    prediction.value.homeTeamVoteCount * 100 / totalVotes
                prediction.value.awayTeamVotePercentage =
                    prediction.value.awayTeamVoteCount * 100 / totalVotes
            } else {
                Log.d("SingleMatchViewModel", "totalVotes = 0")
            }
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
            sendPredictionToFirestore(prediction.value, matchID!!.removePrefix("{matchID}").toInt())
            getPrediction()
        }
    }
}