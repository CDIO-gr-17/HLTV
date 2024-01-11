package com.example.hltv.ui.screens.singleEvent

import android.graphics.Bitmap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hltv.data.convertTimestampToDateClock
import com.example.hltv.data.remote.APIResponse
import com.example.hltv.data.remote.Season
import com.example.hltv.data.remote.ThirdUniqueTournament
import com.example.hltv.data.remote.UniqueTournament
import com.example.hltv.data.remote.UniqueTournamentInfo
import com.example.hltv.data.remote.getTournamentInfo
import com.example.hltv.data.remote.getTournamentLogo
import com.example.hltv.data.remote.getUniqueTournamentDetails
import com.example.hltv.data.remote.getUniqueTournamentSeasons
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SingleEventViewModel: ViewModel() {


    val eventDetails = mutableStateOf<UniqueTournamentInfo>(UniqueTournamentInfo())
    val event = mutableStateOf<ThirdUniqueTournament>(ThirdUniqueTournament())
    val tournamentImage = mutableStateOf<Bitmap?>(null)
    var seasons = mutableListOf<Season>()
    var tournamentSeason = mutableStateOf<Season>(Season())

    val startTime = mutableStateOf("")
    val endTime = mutableStateOf("")
    fun init() {

    }

    fun loadData(tournamentID: Int, seasonID: Int){
        viewModelScope.launch(Dispatchers.IO) {
            eventDetails.value = getUniqueTournamentDetails(tournamentID, seasonID).uniqueTournamentInfo
            seasons.addAll(getUniqueTournamentSeasons(tournamentID).seasons)
            event.value = getTournamentInfo(tournamentID).tournamentDetails
            tournamentImage.value = getTournamentLogo(tournamentID)
            val season = seasons.find {it.id == seasonID}
            if (season != null){
                tournamentSeason.value = season
            }

            startTime.value = convertTimestampToDateClock(event.value.startDateTimestamp)
            endTime.value = convertTimestampToDateClock(event.value.endDateTimestamp)


        }
    }
}





