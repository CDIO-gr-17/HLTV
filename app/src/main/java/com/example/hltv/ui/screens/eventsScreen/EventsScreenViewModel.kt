package com.example.hltv.ui.screens.eventsScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.hltv.data.remote.getCSTournamentsID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch




suspend fun TournamentID(): List<Int> {
    val catID = getCSTournamentsID(1572)
    return catID
}

class EventsScreenViewModel : ViewModel() {
    var test = mutableListOf<Int>(2,3,4)

    init {
        CoroutineScope(Dispatchers.IO).launch {

            for (i in TournamentID()) {
                Log.d("Test", "test " + i.toString())
                test.add(i)
            }

        }
    }



}