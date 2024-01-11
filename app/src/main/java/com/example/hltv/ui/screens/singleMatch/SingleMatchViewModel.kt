package com.example.hltv.ui.screens.singleMatch

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hltv.data.convertTimestampToDateClock
import com.example.hltv.data.remote.Event
import com.example.hltv.data.remote.Game
import com.example.hltv.data.remote.Media
import com.example.hltv.data.remote.Prediction
import com.example.hltv.data.remote.getEvent
import com.example.hltv.data.remote.getGamesFromEvent
import com.example.hltv.data.remote.getMapImageFromMapID
import com.example.hltv.data.remote.getPredictionFromFirestore
import com.example.hltv.data.remote.getTeamImage
import com.example.hltv.data.remote.getTeamMedia
import com.example.hltv.data.remote.getTournamentLogo
import com.example.hltv.data.remote.sendPredictionToFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SingleMatchViewModel() : ViewModel() {
    var prediction: MutableState<Prediction> = mutableStateOf(Prediction(0, 0))
    val games = mutableListOf<Game>()
    val mapImages = mutableStateListOf<Bitmap?>(null)
    var event = mutableStateOf<Event?>(null)
    var LiveEvent = mutableStateOf<Event?>(null)
    var UpcomingEvent = mutableStateOf<Event?>(null)
    var FinishedEvent = mutableStateOf<Event?>(null)
    var awayTeamIcon = mutableStateOf<Bitmap?>(null)
    var homeTeamIcon = mutableStateOf<Bitmap?>(null)
    var tournamentIcon = mutableStateOf<Bitmap?>(null)
    var description = ""
    var attendingTeams = mutableStateListOf<String?>(null)


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
                event.value = getEvent(niceMatchID).event!!
                Log.i(
                    "SingleMatchViewModel",
                    "Loaded niceMatch, ${event.value!!.homeTeam.name} vs ${event.value!!.awayTeam.name}, ${event.value?.homeScore?.display} - ${event.value?.awayScore?.display}"
                )
                homeTeamIcon.value = getTeamImage(event.value!!.homeTeam.id)
                awayTeamIcon.value = getTeamImage(event.value!!.awayTeam.id)
                tournamentIcon.value = getTournamentLogo(event.value!!.tournament.uniqueTournament?.id)
                getPrediction(matchID)
                attendingTeams.add(event.value!!.tournament.category?.flag)
                Log.i("flag","")
                if (event.value!!.status?.type == "finished") { // Match with description "ended" has finished
                    FinishedEvent.value = event.value
                    Log.i(
                        "SingleMatchViewModel",
                        "${event.value!!.status?.description} match added to finished events"
                    )
                } else if (event.value!!.status?.type == "inprogress") { // Match is not started
                    LiveEvent.value = event.value
                    Log.i(
                        "SingleMatchViewModel",
                        "${event.value!!.startTimestamp!!} > ${System.currentTimeMillis() / 1000}. ${event.value!!.status?.description}. Match added to live events."
                    )
                } else { // Match must be upcoming
                    UpcomingEvent.value = event.value
                    Log.i("SingleMatchViewModel", "Match added to upcoming events")
                    description =
                        "${event.value!!.homeTeam.name} will be playing against ${event.value!!.awayTeam.name}" +
                                " at ${convertTimestampToDateClock(event.value!!.startTimestamp)} in the ${event.value!!.tournament.name} tournament." +
                                " They will be playing in a best of ${event.value!!.bestOf} map format."
                }
                _tournamentMedia.value =
                    getMedia(event.value!!.homeTeam.id, event.value!!.awayTeam.id)
            }
        }
    }

    fun loadGames(matchID: String?) {
        val niceMatchID = matchID!!.toInt()
        viewModelScope.launch(Dispatchers.IO) {
            games.addAll(getGamesFromEvent(niceMatchID).games)
            mapImages.clear()
            val tempGames = List(getGamesFromEvent(niceMatchID).games.size) { Game() }
            games.forEach { game ->
                if (game.map?.id != null) {
                    val mapImage = getMapImageFromMapID(game.map?.id!!)
                    if (mapImage != null) {
                        mapImages.add(mapImage)
                        Log.i(
                            "mapImage",
                            "Added mapImage $mapImage from ID ${game.map?.id} Name ${game.map?.name}"
                        )
                    } else Log.d("SingleMatchViewModel", "Map image is null")
                } else Log.d(
                    "SingleMatchViewModel",
                    "Map ID is null for game with ID ${game.id}"
                )
            }
            getPrediction(niceMatchID.toString())
        }
    }

    private suspend fun getMedia(homeTeamID: Int?, awayTeamID: Int?): ArrayList<Media> {
        val allMedia = getTeamMedia(homeTeamID).media
        allMedia.addAll(getTeamMedia(awayTeamID).media)
        return allMedia
    }
}