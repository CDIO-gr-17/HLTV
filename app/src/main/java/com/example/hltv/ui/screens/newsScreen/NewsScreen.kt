package com.example.hltv.ui.screens.newsScreen

import androidx.compose.runtime.Composable
import com.example.hltv.ui.screens.singleTeamScreen.SingleTeamScreen

@Composable
fun NewsScreen(onClickSinglePlayer: (playerID: String?) -> Unit, onClickSingleTeam: (teamID: String?) -> Unit) {
    SingleTeamScreen(teamID = "364378" ,{onClickSinglePlayer("1078255")}, {onClickSingleTeam("PlaceholderID")}){
    }
}