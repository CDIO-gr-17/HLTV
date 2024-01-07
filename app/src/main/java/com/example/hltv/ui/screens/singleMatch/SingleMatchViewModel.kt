package com.example.hltv.ui.screens.singleMatch

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hltv.data.remote.Event
import com.example.hltv.data.remote.Prediction
import com.example.hltv.data.remote.getEvent
import com.example.hltv.data.remote.getPredictionFromFirestore
import com.example.hltv.data.remote.getTeamImage
import com.example.hltv.data.remote.sendPredictionToFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SingleMatchViewModel(var matchID: String?) : ViewModel() {
    var prediction: MutableState<Prediction> = mutableStateOf(Prediction(0, 0))
    val niceMatchID = matchID!!.removePrefix("{matchID}").toInt()
    var event = mutableStateOf<Event?>(null)
    var awayTeamIcon =  mutableStateOf<Bitmap?>(null)
    var homeTeamIcon =  mutableStateOf<Bitmap?>(null)
    val en = 1


    init {
        CoroutineScope(Dispatchers.IO).launch {

            getPrediction()
        }
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
    fun loadData(){
        viewModelScope.launch{
            CoroutineScope(Dispatchers.IO).launch {
                //Det ik for sjov det her
                event.value = getEvent(niceMatchID).event!!
                homeTeamIcon.value = getTeamImage(event.value!!.homeTeam.id)
                awayTeamIcon.value = getTeamImage(event.value!!.awayTeam.id)
            }
        }
    }
}