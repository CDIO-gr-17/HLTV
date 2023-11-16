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
             Log.i("RankingScreen", "Size of liveMatches is: " + prevMatches.events.size.toString())
             for ((index, event) in prevMatches.events.withIndex()) {
                 Log.i("RankingScreen","Adding string with event" + index.toString() + ". Name is: " + event.winnerCode.toString()+ " VS " + event.time)
                 matchResult.add(event.winnerCode.toString() + " VS " + event.awayTeam.name)
             }
         }



     }



 }

}