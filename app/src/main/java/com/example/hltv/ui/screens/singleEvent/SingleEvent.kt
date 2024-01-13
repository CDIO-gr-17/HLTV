package com.example.hltv.ui.screens.singleEvent


import android.graphics.BitmapShader
import android.graphics.Paint
import android.graphics.Shader
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.palette.graphics.Palette
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.hltv.R
import com.example.hltv.data.convertTimestampToDateClock
import com.example.hltv.data.getFlagFromCountryCode
import com.example.hltv.ui.common.CommonCard
import com.example.hltv.ui.screens.singleTeamScreen.OverviewInfo
import com.example.hltv.ui.screens.singleTeamScreen.OverviewPlayer
import com.example.hltv.ui.screens.singleTeamScreen.Player
import com.example.hltv.ui.screens.singleTeamScreen.RecentMatch
import com.example.hltv.ui.screens.singleTeamScreen.RecentMatches
import com.example.hltv.ui.screens.singleTeamScreen.SingleTeamViewModel
import com.example.hltv.ui.screens.singleTeamScreen.Statistics
import com.example.hltv.ui.screens.singleTeamScreen.Stats
import com.example.hltv.ui.screens.singleTeamScreen.TeamOverview

@Preview
@Composable
fun Show(){
    val viewModel: SingleEventViewModel = viewModel()
    //SingleEventTopbox(viewModel, {})
}

@Composable
fun SingleEventScreen (tournamentID: String?,
                       seasonID: String?,
                       onClickSingleTeam : (String) -> Unit,
                       onClickSingleMatch : (String) -> Unit) {
    Log.i("SingleEventScreen", "TournamentID: " + tournamentID)
    Log.i("SingleEventScreen", "seasonID: " + seasonID)

    val eventViewModel: SingleEventViewModel = viewModel()
    val teamViewModel : SingleTeamViewModel = viewModel()

    LaunchedEffect(Unit) {
        eventViewModel.loadData(tournamentID!!.toInt(), seasonID!!.toInt())
    }
    LaunchedEffect(eventViewModel.eventDetails.value.winner?.id){
        if(eventViewModel.eventDetails.value.winner?.id != null){
            Log.i("SingleEventScreen", "The value: " + eventViewModel.eventDetails.value.toString())
            teamViewModel.loadData(eventViewModel.eventDetails.value.winner?.id.toString())
        }
    }

    //TODO: Make this prettier?



    val recentMatches = teamViewModel.recentMatches
    val playerOverview = teamViewModel.playerOverview
    val statsOverview = teamViewModel.statisticsOverview
    val countryCode = statsOverview.value.countryCode
    val countryFlag = getFlagFromCountryCode(countryCode = countryCode)
    val standings = eventViewModel.standings





    Column {

        if (standings.isNotEmpty()) {
            val teamNames = standings.flatMap { standing ->
                standing.attending.mapNotNull { it.team?.name }
            }
            Column {
                teamNames.forEach { name ->
                    Text(text = name, color = MaterialTheme.colorScheme.errorContainer)
                    Log.i("tournamentStandings", name)
                }
            }
        }
        else Text(text = "No standings",  color = MaterialTheme.colorScheme.errorContainer)
        SingleEventTopbox(
            playerOverview = playerOverview,
            teamViewModel = teamViewModel,
            statsOverview = statsOverview,
            onClickSinglePlayer = { Log.i("SingleEventScreen", "Clicked onClickSinglePlayer") },
            onClickSingleTeam = { Log.i("SingleEventScreen", "Clicked onClickSingleTeam") },
            onClickSingleMatch = { Log.i("SingleEventScreen", "Clicked onClickSingleMatch") },
            painter = countryFlag,
            recentMatches = recentMatches,
            viewModel = eventViewModel
        )
    }





}

@Composable
fun SingleEventTopbox(viewModel: SingleEventViewModel,
                      teamViewModel: SingleTeamViewModel,
                      playerOverview : SnapshotStateList<Player>,
                      statsOverview : MutableState<Stats>,
                      onClickSinglePlayer: (String?) -> Unit,
                      onClickSingleTeam: (String?) -> Unit,
                      onClickSingleMatch: (String?) -> Unit,
                      painter : AsyncImagePainter,
                      recentMatches : SnapshotStateList<RecentMatch>,

) {

    LazyColumn {

        item {
            CommonCard(
                modifier = Modifier,
                customInnerPadding = 0.dp,
                customOuterPadding = 0.dp,
                topBox = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Spacer(Modifier.height(15.dp))

                        Box(/*modifier = Modifier.height(200.dp)*/) {
                            Image(
                                painter = rememberAsyncImagePainter(viewModel.tournamentImage.value),
                                contentDescription = "Event logo",
                                modifier = Modifier.size(200.dp),
                                //.clipToBounds(),/*offset(y = (-45).dp)*/
                                //.size(300.dp)
                                contentScale = ContentScale.Crop
                            )
                        }

                        val tournamentColors = listOf(
                            Color.White,
                            if (viewModel.palette.value?.vibrantSwatch?.rgb != null) Color(
                                viewModel.palette.value?.vibrantSwatch?.rgb!!
                            ) else Color.Black
                        )

                        Text(//Tournament name
                            textAlign = TextAlign.Center,
                            //text = "${viewModel.event.value.name} ${viewModel.tournamentSeason.value.name}",
                            text = buildAnnotatedString {
                                withStyle(ParagraphStyle(lineHeight = 46.sp)) {
                                    withStyle(
                                        style = SpanStyle(
                                            brush = Brush.verticalGradient(
                                                colors = tournamentColors
                                            ),
                                            fontSize = 65.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                        ),
                                    ) {
                                        append(viewModel.event.value.name.toString().uppercase())
                                    }
                                }
                                withStyle(

                                    style = SpanStyle(
                                        brush = Brush.verticalGradient(
                                            colors = tournamentColors.reversed()
                                        ),
                                        fontSize = 30.sp,
                                        fontWeight = FontWeight.Bold,
                                    ),
                                ) {
                                    append(
                                        viewModel.tournamentSeason.value.name.toString().uppercase()
                                    )
                                }
                            }
                        )

                        Text(
                            textAlign = TextAlign.Center,
                            text = viewModel.eventDetails.value.totalPrizeMoney.toString() + " " + viewModel.eventDetails.value.totalPrizeMoneyCurrency.toString(),
                            color = Color.White
                        )
                        Text(
                            textAlign = TextAlign.Center,
                            text = if (!viewModel.startTime.value.contains("Unknown")) (viewModel.startTime.value + " - " + viewModel.endTime.value) else "",
                            color = Color.White
                        )

                        Column(
                            //horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {


                            //COPY PASTE FROM SINGLETEAM
                            //I tried doing DI, but we cant because the lazycolumn needs an argument
                            //that you cant pass so wed need DI in the DI and that was too much work

                            Column(modifier = Modifier.fillParentMaxHeight()) {

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    //horizontalArrangement = Arrangement.SpaceBetween
                                ) {


                                    Image(
                                        rememberAsyncImagePainter(
                                            teamViewModel.teamImage.value
                                        ),
                                        contentDescription = teamViewModel.team.value.name,
                                        Modifier.size(69.dp)
                                    )


                                    val gradientColors = listOf(
                                        Color.White,
                                        if (teamViewModel.palette.value?.vibrantSwatch?.rgb != null) Color(
                                            teamViewModel.palette.value?.vibrantSwatch?.rgb!!
                                        ) else Color.Black
                                    )

                                    Text(
                                        modifier = Modifier,
                                        text = buildAnnotatedString {
                                            append("Winner\n\n")
                                            withStyle(
                                                style = SpanStyle(
                                                    brush = Brush.linearGradient(
                                                        colors = gradientColors
                                                    ),
                                                    fontSize = 65.sp,
                                                    fontWeight = FontWeight.ExtraBold
                                                ),
                                            ) {
                                                append(if (teamViewModel.team.value.name != null) teamViewModel.team.value.name else "")
                                            }
                                        }
                                    )
                                }


                                CommonCard(modifier = Modifier, bottomBox = {
                                    Column {
                                        LazyRow {
                                            items(playerOverview.size) { index ->
                                                OverviewPlayer(
                                                    player = playerOverview[index],
                                                    onClickSinglePlayer
                                                )
                                            }
                                        }
                                        Statistics(
                                            coach = "Peter 'Castle' Ardenskjold",
                                            points = "1000",
                                            winRate = "61%",
                                            bestMap = "Overpass",
                                            averagePlayerAge = statsOverview.value.avgAgeofPlayers,
                                            imageNat = painterResource(R.drawable.dk_flag)
                                        )
                                        Text(
                                            text = "Recent Matches",
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                        recentMatches.forEachIndexed{ index, match ->
                                            RecentMatches(
                                                modifier = Modifier.clickable {
                                                    onClickSingleMatch(
                                                        recentMatches[index].matchID.toString()
                                                    )
                                                },
                                                team1 = recentMatches[index].homeTeam?.name,
                                                team2 = recentMatches[index].awayTeam?.name,
                                                imageTeam1 = rememberAsyncImagePainter(
                                                    recentMatches[index].homeTeamImage
                                                ),
                                                imageTeam2 = rememberAsyncImagePainter(
                                                    recentMatches[index].awayTeamImage
                                                ),
                                                team2OnClick = {
                                                    onClickSingleTeam(
                                                        recentMatches[index].awayTeam?.id.toString()
                                                    )
                                                },
                                                score = recentMatches[index].homeScore?.display.toString() + " - " + recentMatches[index].awayScore?.display.toString(),
                                                date = recentMatches[index].startTimestamp.toString()
                                            )
                                        }

                                    }
                                })
                            }
                        }
                    }
                }
            )
        }
    }
}






