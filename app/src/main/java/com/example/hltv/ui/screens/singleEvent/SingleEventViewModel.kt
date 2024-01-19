package com.example.hltv.ui.screens.singleEvent

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.example.hltv.data.convertTimestampToDateURL
import com.example.hltv.data.remote.Season
import com.example.hltv.data.remote.Standings
import com.example.hltv.data.remote.ThirdUniqueTournament
import com.example.hltv.data.remote.UniqueTournamentInfo
import com.example.hltv.data.remote.getTournamentInfo
import com.example.hltv.data.remote.getTournamentLogo
import com.example.hltv.data.remote.getTournamentStandings
import com.example.hltv.data.remote.getUniqueTournamentDetails
import com.example.hltv.data.remote.getUniqueTournamentSeasons
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

class SingleEventViewModel : ViewModel() {


    val eventDetails = mutableStateOf(UniqueTournamentInfo())
    val event = mutableStateOf(ThirdUniqueTournament())
    val tournamentImage = mutableStateOf<Bitmap?>(null)
    private var seasons = mutableListOf<Season>()
    var tournamentSeason = mutableStateOf(Season())
    var standings = mutableStateListOf<Standings>()
    val color = mutableStateOf(Color.White)


    val startTime = mutableStateOf("")
    val endTime = mutableStateOf("")
    private var isLoaded = false

    fun loadData(tournamentID: Int, seasonID: Int) {
        if (isLoaded) return
        isLoaded = true
        viewModelScope.launch(Dispatchers.IO) {
            val tournamentStandings = getTournamentStandings(
                tournamentID,
                seasonID
            ).standings //Only a few tournaments actually show standings
            if (tournamentStandings.isNotEmpty())
                standings.addAll(tournamentStandings)
            eventDetails.value =
                getUniqueTournamentDetails(tournamentID, seasonID).uniqueTournamentInfo
            try {
                seasons.addAll(getUniqueTournamentSeasons(tournamentID).seasons)
            } catch (e: IOException) {
                Log.w("SingleEventViewModel.loadData", "There was no season info: $e")
            }
            try {
                event.value = getTournamentInfo(tournamentID).tournamentDetails
            } catch (e: IOException) {
                Log.w("SingleEventViewModel.loadData", "There was no tournament info: $e")
            } catch (e : ClassCastException){
                Log.w("SingleEventViewModel.loadData", "There was no tournament info: $e")

            }

            tournamentImage.value = getTournamentLogo(tournamentID)

            /*
            if (homeTeamIcon.value != null){
                val homeTeamPalette = Palette.from(homeTeamIcon.value!!).generate()
                if (homeTeamPalette.vibrantSwatch?.rgb != null) {
                    homeTeamColor.value = Color(homeTeamPalette.vibrantSwatch?.rgb!!)
                } else homeTeamColor.value = Color.Blue
            } else homeTeamColor.value = Color.Blue
             */
            if (tournamentImage.value != null){
                val palette = Palette.from(tournamentImage.value!!).generate()
                if (tournamentImage.value != null && palette.vibrantSwatch?.rgb != null) {
                    color.value = Color(palette.vibrantSwatch?.rgb!!)
                } else {color.value = Color.Cyan}
            } else color.value = Color.Cyan


            val season = seasons.find { it.id == seasonID }
            if (season != null) {
                tournamentSeason.value = season
            }
            startTime.value = convertTimestampToDateURL(event.value.startDateTimestamp)
            endTime.value = convertTimestampToDateURL(event.value.endDateTimestamp)
        }
    }
}





