package com.example.hltv.ui.screens.eventsScreen

import androidx.lifecycle.ViewModel
import com.example.hltv.ui.screens.singleEvent.SingleEvent

data class EventsScreen(
    val ongoingEvents : List<SingleEvent>,
    val upcomingEvents : List<SingleEvent>
)

class EventsScreenViewModel : ViewModel() {


}