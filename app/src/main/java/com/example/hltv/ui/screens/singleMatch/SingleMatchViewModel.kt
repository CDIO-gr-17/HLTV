package com.example.hltv.ui.screens.singleMatch

import android.util.Log
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hltv.data.remote.APIResponse
import com.example.hltv.data.remote.getLiveMatches
import com.example.hltv.data.remote.getPreviousMatches
import com.example.hltv.data.remote.getTeamImage
import com.example.hltv.ui.screens.teamsScreen.AllPlayerImages
import com.example.hltv.ui.screens.teamsScreen.TeamPlayerImages
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.Dispatcher




class SingleMatchViewModel: ViewModel() {
    private val _matches = MutableStateFlow<List<APIResponse.EventsWrapper>>(emptyList())
    val matches =_matches.asStateFlow()
    val matchResult =  mutableStateListOf(" ", " ", " ")

 init{
     CoroutineScope(Dispatchers.IO).launch {
         // Simulate loading data

         val prevMatches = getPreviousMatches(364425,0)
         matchResult.clear()


         if (prevMatches != null) {
             Log.i("Singlematchscreen", "This teams: 364425 " + prevMatches.events.size.toString())
             for ((index, event) in prevMatches.events.withIndex()) {
                 Log.i("Sinlgematchscreen","Adding string with event" + index.toString() + ". Name is: "+ " VS " + event.homeScore)
                 matchResult.add(event.homeTeam.name + " VS " + event.awayTeam.name + event.homeScore)
             }

         }


     }

 }
    }
/*
suspend fun getAllTeamImages(eventsWrapper: APIResponse.EventsWrapper):AllPlayerImages{


    var list = TeamPlayerImages(mutableListOf(),event.id,event.homeTeam.name)
    for (event in eventsWrapper.events){

        val bitmap1 = getTeamImage(event.homeTeam.id)
        val bitmap2 = getTeamImage(event.awayTeam.id)
        list.teamImages?.add(bitmap1)
        list.teamImages?.add(bitmap2)

    }
    return list
*/



