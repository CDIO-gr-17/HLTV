package com.example.hltv.ui.screens.newsScreen

import androidx.compose.runtime.Composable
import com.example.hltv.ui.screens.singleTeamScreen.SingleTeamScreen

@Composable
fun NewsScreen(onClickSinglePlayer: (playerID: String?) -> Unit) {
    SingleTeamScreen(teamID = "PlaceHolderID"){
        onClickSinglePlayer("PlaceHolderID")
    }
}