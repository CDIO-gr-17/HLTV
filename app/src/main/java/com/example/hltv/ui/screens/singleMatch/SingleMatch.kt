package com.example.hltv.ui.screens.singleMatch

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.hltv.R

@Composable
fun SingleMatchScreen(matchID : String?){
    val viewModel = SingleMatchViewModel(matchID)

    PredictionCard(teamOneIcon = painterResource(id = R.drawable.astralis_logo), teamTwoIcon = painterResource(
        id = R.drawable.astralis_logo
    ) , viewModel = viewModel)

}

@Preview(showBackground = true)
@Composable
fun SingleMatchScreenPreview() {



}