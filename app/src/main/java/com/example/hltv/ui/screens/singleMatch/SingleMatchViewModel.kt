package com.example.hltv.ui.screens.singleMatch

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.hltv.data.remote.Prediction
import com.example.hltv.data.remote.getPredictionFromFirestore
import com.example.hltv.data.remote.sendPredictionToFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SingleMatchViewModel : ViewModel() {
    var prediction: MutableState<Prediction> = mutableStateOf(Prediction(0, 0))


    /*
    private val _matches = MutableStateFlow<List<APIResponse.EventsWrapper>>(emptyList())
    val matches = _matches.asStateFlow()
    val matchResult = mutableStateListOf(" ", " ", " ")
*/

    init {
        CoroutineScope(Dispatchers.IO).launch {

        }


    }

    fun getPrediction(matchID: Int) {
        val tempPrediction = getPredictionFromFirestore(matchID)
        if (tempPrediction == null) {
            prediction.value = Prediction(0, 0)
            return
        } else {
            prediction.value = tempPrediction
        }
        val totalVotes = prediction.value.homeTeamVoteCount + prediction.value.awayTeamVoteCount
        prediction.value = Prediction(
            prediction.value.homeTeamVoteCount * 100 / totalVotes,
            prediction.value.awayTeamVoteCount * 100 / totalVotes
        )
    }
    fun updatePrediction(matchID: Int, vote: Int) {
        if (vote == 1) {
            prediction.value = Prediction(prediction.value.homeTeamVoteCount + 1, prediction.value.awayTeamVoteCount)
        } else if (vote == 2) {
            prediction.value = Prediction(prediction.value.homeTeamVoteCount, prediction.value.awayTeamVoteCount + 1)
        } else {
            return
        }
        sendPredictionToFirestore(prediction.value, matchID)
    }
}