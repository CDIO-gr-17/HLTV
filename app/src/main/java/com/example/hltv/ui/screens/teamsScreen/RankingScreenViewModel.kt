package com.example.hltv.ui.screens.teamsScreen

import android.content.Context
import android.graphics.Bitmap
import android.media.Image
import android.os.SystemClock.sleep
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.lifecycle.ViewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.hltv.data.remote.getLiveMatches
import com.example.hltv.data.remote.getPlayerImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.EnumSet.range

data class img(
    val bitMap: Bitmap?
)
class RankingScreenViewModel: ViewModel() {
    val teamNames = mutableStateListOf("1", "2", "3", "4", "5")
    private val _playerImage = MutableStateFlow<img>(img(null))
    val playerImage = _playerImage.asStateFlow()

    init{
        CoroutineScope(Dispatchers.IO).launch{
            _playerImage.value = img(
                getPlayerImage())

        }
        CoroutineScope(Dispatchers.IO).launch {

            val liveMatches = getLiveMatches()
            teamNames.clear()
            if (liveMatches != null && liveMatches.events!=null) {
                //Log.i("RankingScreen", "Size of liveMatches is: " + liveMatches.events.size.toString())

                //TODO: This loop is called multiple times. Pretty painful
                //Whole initializer is called multiple times, but stopping that breaks it
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