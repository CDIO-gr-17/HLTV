package com.example.hltv.ui.screens.singleEvent

data class SingleEventViewModel(
    val title : String,
    val date : String,
    val location : String,
    val prizePool : String,
    val attendingTeams : List<String>,
    val ongoing : Boolean
    )
