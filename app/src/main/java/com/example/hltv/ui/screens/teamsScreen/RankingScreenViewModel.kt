package com.example.hltv.ui.screens.teamsScreen

import android.content.Context
import android.graphics.Bitmap
import android.os.SystemClock.sleep
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.hltv.data.remote.getLiveMatches
import com.example.hltv.data.remote.getPlayerImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.EnumSet.range
var initialized = false
class RankingScreenViewModel: ViewModel() {
    val teamNames = mutableStateListOf("1", "2", "3", "4", "5")
    init{

        if (!initialized){

            CoroutineScope(Dispatchers.IO).launch {
                val liveMatches = getLiveMatches();
                teamNames.clear()
                if (liveMatches != null && liveMatches.events!=null) {
                    //Log.i("RankingScreen", "Size of liveMatches is: " + liveMatches.events.size.toString())

                    //TODO: This loop is called multiple times. Pretty painful
                    //Whole initializer is called multiple times
                    for ((index, event) in liveMatches.events.withIndex()) {
                        Log.i("RankingScreen","Adding string with event " + index.toString() + ". Name is: " + event.homeTeam.name + " VS " + event.awayTeam.name)
                        teamNames.add(event.homeTeam.name + " VS " + event.awayTeam.name)
                    }
                }else{
                    Log.i(this.toString(),"There were no live matches?")
                }
            }
        }
    }
}