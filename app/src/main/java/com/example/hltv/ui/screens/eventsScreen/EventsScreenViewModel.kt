package com.example.hltv.ui.screens.eventsScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.hltv.data.remote.getCSCategory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch




suspend fun CatID(): Int {
    val catID = getCSCategory()
    return catID
}

class EventsScreenViewModel : ViewModel() {
    var test : Int? = null

    init {
        CoroutineScope(Dispatchers.IO).launch {
            Log.d("Test","test "+test.toString())
            test = CatID()

        }
    }



}