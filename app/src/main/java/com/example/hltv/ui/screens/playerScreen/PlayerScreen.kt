package com.example.hltv.ui.screens.playerScreen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import com.example.hltv.R
import com.example.hltv.ui.common.CommonCard
import coil.compose.rememberAsyncImagePainter


@Composable
fun PlayerScreen(
    playerID: String?
){
    val viewModel = PlayerScreenViewModel(playerID);
    Log.i("", "Transferred player \"ID\": " + playerID)
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxHeight()
    ){
        PlayerImage(image = AsyncImagePainter(viewModel.singlePlayerData.value.playerImage))
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.SpaceEvenly
        ){
            Box(modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ){
                TeamBox(
                    team = ("Player: " + playerID),
                    teamWorldRank = "5",
                    teamNationality = "Denmark",
                )
            }
            Box (modifier = Modifier
                .fillMaxWidth()
                .weight(1f))
            {
                StatsBox(
                    name = "Dean big mama",
                    rating = "2,0",
                    KD = "1,45"
                )
            }

        }
    }
}

@Composable
fun PlayerImage(
    image: Painter ?= null,
){
    Image(
        painter = image ?: painterResource(id = R.drawable.person_24px),
        contentDescription = null,
        modifier = Modifier
            .width(300.dp)
            .height(300.dp)
    )
}

@Composable
fun TeamBox(
    team: String,
    teamWorldRank: String,
    teamNationality: String,
){
    CommonCard (
        modifier = Modifier.fillMaxWidth(),
        headText = "Team",
        bottomBox = {
            Column {
                Text(text = "$team #$teamWorldRank")
                Text(text = "Nationality $teamNationality")
            }
        }
    )
}

@Composable
fun StatsBox(
    name: String ?= null,
    rating: String ?= null,
    KD: String ?= null
){
    CommonCard (
        modifier = Modifier.fillMaxWidth(),
        headText = "Statistics",
        bottomBox = {
            Column {
                name?.let {
                    Text(text = "Name: $name")
                }
                rating?.let{
                    Text(text = "Rating: $rating")
                }
                KD?.let{
                    Text(text = "KD: $KD")
                }
            }
        }
    )
}

@Preview
@Composable
fun PlayerScreenPreview(){
    PlayerScreen("OBAMNA")
}