package com.example.hltv.ui.screens.singleTeam

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import com.example.hltv.data.remote.APIResponse
import com.example.hltv.data.remote.Country
import com.example.hltv.data.remote.PlayerGroup
import com.example.hltv.data.remote.Player_orsub
import com.example.hltv.data.remote.Score
import com.example.hltv.data.remote.Team
import com.example.hltv.data.remote.Time

data class SingleTeam(
    val eventsWrapper: APIResponse.EventsWrapper,
    val players : ArrayList<Player> ?= null,
    val playerImage: Bitmap?= null,
    val country: Country ?= null,
    val dateOfBirthTimestamp : Int ?= null,
    val homeTeam: Team ?= null,
    val awayTeam: Team ?= null,
    val homeScore: Score?= null,
    val awayScore: Score ?= null,
    val time: Time ?= null,
    val bestOf: Int ?= null,
    )
class SingleTeamViewModel: ViewModel() {


}