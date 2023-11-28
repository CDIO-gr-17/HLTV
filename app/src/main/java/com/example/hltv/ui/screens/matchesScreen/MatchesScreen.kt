package com.example.hltv.ui.screens.matchesScreen
import android.graphics.Bitmap
import android.os.SystemClock.sleep
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.hltv.R
import com.example.hltv.ui.common.LiveMatchCard


data class ListItem(val ranking: Int, val text1: String, val text2: String)

@Composable
fun MatchesScreen(onClickSingleTeam : (String?) -> Unit) {
    val viewModel = MatchesScreenViewModel()
    val teamValues = viewModel.teamValues

/*
    val allPlayerImages = viewModel.allPlayerImages.collectAsState()
    val playerbmap = viewModel.playerImage.collectAsState()*/

    LazyColumn {
        items(teamValues) { item  ->
            LiveMatchCard(
                modifier = Modifier,
                teamOneName = item.homeTeam.name.toString(),
                teamOneIcon =  rememberAsyncImagePainter(viewModel.homeTeamIcons[teamValues.indexOf(item)]),             //TODO: Needs TeamLogo
                teamOneScore = item.homeScore!!.display!!.toInt(),
                teamOneOnClick = { onClickSingleTeam(item.homeTeam.id.toString()) },
                teamTwoName = item.awayTeam.name.toString(),
                teamTwoIcon = rememberAsyncImagePainter(viewModel.awayTeamIcons[teamValues.indexOf(item)]),             //TODO: Needs TeamLogo
                teamTwoScore = item.awayScore!!.current!!.toInt(),
                teamTwoOnClick = { onClickSingleTeam(item.awayTeam.id.toString()) },
            )

        }
    }


            }
           /* if (index < viewModel.teamNames.size - 1) {
                Spacer(modifier = Modifier.height(1.dp))*/
        /*
        CoroutineScope(Dispatchers.IO).launch {
            // Simulate loading data
            val liveMatches = getLiveMatches();
            if (liveMatches != null) {
                Log.i("RankingScreen", "Size of liveMatches is: " + liveMatches.events.size.toString())
                teamNames.removeAt(0)

                for ((index, event) in liveMatches.events.withIndex()) {
                    Log.i("RankingScreen","Adding string with event" + index.toString() + ". Name is: " + event.homeTeam.name + " VS " + event.awayTeam.name)
                    teamNames.add(event.homeTeam.name + " VS " + event.awayTeam.name)
                }
            }


        }
         */



@Composable
fun teamCard(
    modifier: Modifier,
    materialTheme: MaterialTheme,
    text1: String = " ",
    text2: String,
    singleImgState: State<img>,
    teamPlayerImages: TeamPlayerImages?
    ) =

    Card (
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(131.dp)
    ){
        Box(modifier = Modifier
            .background(color = Color.DarkGray)
            .fillMaxWidth()
            .height(42.dp)
        ) {
            Text(
                text = text1,
                fontSize = materialTheme.typography.bodyLarge.fontSize,
                color = materialTheme.colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 10.dp)
            )

        }
        Row (modifier = Modifier.padding(start = 10.dp)) {

            for (i in 1..5) {
                var bitmap: Bitmap? = null

                if (teamPlayerImages != null) {
                    if (teamPlayerImages.teamImages?.size == 0) {
                        Log.w(
                            "RankingScreen",
                            "TeamImages is of size 0. Sleeping 100ms and hoping for the best"
                        )
                        sleep(100)
                    }
                    if (teamPlayerImages.teamImages?.size != 0) {
                        bitmap = teamPlayerImages.teamImages?.get(i - 1)
                    }

                }

                val painter: AsyncImagePainter = if (bitmap == null){
                    rememberAsyncImagePainter(R.drawable.playersilouhette)
                } else{
                    rememberAsyncImagePainter(bitmap)
                }
                Column (modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)){
                    Image(

                        painter = painter,
                        contentDescription = null, //TODO
                        alignment = Alignment.TopStart,
                        modifier = Modifier
                            .size(69.dp)
                            .offset(y = 10.dp)
                    )
                    Card (
                        modifier = modifier
                            .fillMaxWidth()
                            .height(12.dp)
                            .padding(end = 10.dp)
                    ) {
                        Box(modifier = Modifier
                            .background(color = Color.LightGray)
                            .fillMaxWidth()
                        ) {
                            Text(
                                text = "Name",
                                fontSize = materialTheme.typography.bodySmall.fontSize,
                                color = materialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .offset(y = (-0.5).dp) //Makes text look more centered, I think?
                                    .wrapContentSize(unbounded = true) //Renders bottom of text
                            )
                        }
                    }
                }
            }
        }
    }

@Preview(showBackground = true)
@Composable
fun MatchesScreenPreview() {
   // MatchesScreen(onClickSingleTeam = print(""))
}