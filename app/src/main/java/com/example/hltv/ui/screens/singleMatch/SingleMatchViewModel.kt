package com.example.hltv.ui.screens.singleMatch

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hltv.data.remote.Event
import com.example.hltv.data.remote.Media
import com.example.hltv.data.remote.Prediction
import com.example.hltv.data.remote.getEvent
import com.example.hltv.data.remote.getPredictionFromFirestore
import com.example.hltv.data.remote.getTeamImage
import com.example.hltv.data.remote.getTeamMedia
import com.example.hltv.data.remote.sendPredictionToFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SingleMatchViewModel() : ViewModel() {
    var prediction: MutableState<Prediction> = mutableStateOf(Prediction(0, 0))
    var event = mutableStateOf<Event?>(null)
    var awayTeamIcon = mutableStateOf<Bitmap?>(null)
    var homeTeamIcon = mutableStateOf<Bitmap?>(null)

    private var _tournamentMedia = MutableStateFlow(ArrayList<Media>())
    var tournamentMedia: MutableStateFlow<ArrayList<Media>> = _tournamentMedia

    fun getPrediction(matchID: String?) {
        val niceMatchID = matchID!!.toInt()
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

    fun updatePrediction(vote: Int, matchID: String?) {
        val niceMatchID = matchID!!.toInt()
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
        if (totalVotes == 0) {
            Log.d("SingleMatchViewModel", "totalVotes = 0")
            return
        }
        prediction.homeTeamVotePercentage =
            prediction.homeTeamVoteCount * 100 / totalVotes
        prediction.awayTeamVotePercentage =
            prediction.awayTeamVoteCount * 100 / totalVotes
    }

    fun loadData(matchID: String?) {
        val niceMatchID = matchID!!.toInt()
        viewModelScope.launch {
            CoroutineScope(Dispatchers.IO).launch {
                //Det ik for sjov det her
                event.value = getEvent(niceMatchID).event!!
                Log.i(
                    "SingleMatchViewModel",
                    "Loaded niceMatch, ${event.value!!.homeTeam.name} vs ${event.value!!.awayTeam.name}, ${event.value?.homeScore?.display} - ${event.value?.awayScore?.display}"
                )
                homeTeamIcon.value = getTeamImage(event.value!!.homeTeam.id)
                awayTeamIcon.value = getTeamImage(event.value!!.awayTeam.id)
                getPrediction(matchID)
                _tournamentMedia.value =
                    getMedia(event.value!!.homeTeam.id, event.value!!.awayTeam.id)
            }
        }
    }

    private suspend fun getMedia(homeTeamID: Int?, awayTeamID: Int?): ArrayList<Media> {
        val allMedia = getTeamMedia(homeTeamID).media
        allMedia.addAll(getTeamMedia(awayTeamID).media)
        return allMedia
    }
}