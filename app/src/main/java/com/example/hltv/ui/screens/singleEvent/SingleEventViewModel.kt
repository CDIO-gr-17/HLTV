package com.example.hltv.ui.screens.singleEvent

import androidx.lifecycle.ViewModel
import com.example.hltv.data.remote.Team

data class SingleEvent(
    val title : String,
    val date : String,
    val location : String,
    val prizePool : String,
    val attendingTeams : List<Team>,

    )

class SingleEventViewModel : ViewModel(){

}