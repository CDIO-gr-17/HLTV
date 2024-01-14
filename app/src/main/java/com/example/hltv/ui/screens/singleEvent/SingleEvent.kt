package com.example.hltv.ui.screens.singleEvent


import android.graphics.Color.parseColor
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.hltv.R
import com.example.hltv.data.getColorFromTier
import com.example.hltv.data.getFlagFromCountryCode
import com.example.hltv.ui.common.CommonCard
import com.example.hltv.ui.common.ResizingText
import com.example.hltv.ui.screens.singleTeamScreen.OverviewPlayer
import com.example.hltv.ui.screens.singleTeamScreen.Player
import com.example.hltv.ui.screens.singleTeamScreen.RecentMatch
import com.example.hltv.ui.screens.singleTeamScreen.RecentMatches
import com.example.hltv.ui.screens.singleTeamScreen.SingleTeamViewModel
import com.example.hltv.ui.screens.singleTeamScreen.Statistics
import com.example.hltv.ui.screens.singleTeamScreen.Stats

@Composable
fun SingleEventScreen(
    tournamentID: String?,
    seasonID: String?,
    onClickSingleTeam: (teamID: String?) -> Unit,
    onClickSingleMatch: (matchID: String?) -> Unit
) {
    Log.i("SingleEventScreen", "TournamentID: " + tournamentID)
    Log.i("SingleEventScreen", "seasonID: " + seasonID)

    val eventViewModel: SingleEventViewModel = viewModel()
    val teamViewModel: SingleTeamViewModel = viewModel()

    LaunchedEffect(Unit) {
        eventViewModel.loadData(tournamentID!!.toInt(), seasonID!!.toInt())
    }
    LaunchedEffect(eventViewModel.eventDetails.value.winner?.id) {
        if (eventViewModel.eventDetails.value.winner?.id != null) {
            Log.i("SingleEventScreen", "The value: " + eventViewModel.eventDetails.value.toString())
            teamViewModel.loadData(eventViewModel.eventDetails.value.winner?.id.toString(), 3)
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
            standings.forEach { standing ->
                Column {
                    CommonCard(
                        modifier = Modifier,
                        topBox = {
                                 Column {
                                     Row (modifier = Modifier.fillMaxWidth(),
                                         horizontalArrangement = Arrangement.Center){
                                         Text(
                                             text = "Standings",
                                             fontSize = 18.sp,
                                             color = MaterialTheme.colorScheme.onSecondaryContainer,
                                             fontWeight = FontWeight.Bold
                                         )
                                     }
                                     Row (modifier = Modifier.fillMaxWidth()){
                                         Text(
                                             text = "#",
                                             modifier = Modifier.weight(0.05f),
                                             fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                             color = MaterialTheme.colorScheme.onSecondaryContainer,
                                             fontWeight = FontWeight.Bold
                                         )
                                         Text(
                                             text = "Team",
                                             modifier = Modifier.weight(0.4f),
                                             fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                             color = MaterialTheme.colorScheme.onSecondaryContainer,
                                             fontWeight = FontWeight.Bold
                                         )
                                         Spacer(modifier = Modifier.weight(0.2f))
                                         Text(
                                             text = "P",
                                             modifier = Modifier.weight(0.075f),
                                             fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                             color = MaterialTheme.colorScheme.onSecondaryContainer,
                                             fontWeight = FontWeight.Bold
                                         )
                                         Text(
                                             text = "W",
                                             modifier = Modifier.weight(0.075f),
                                             fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                             color = MaterialTheme.colorScheme.onSecondaryContainer,
                                             fontWeight = FontWeight.Bold
                                         )
                                         Text(
                                             text = "L",
                                             fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                             color = MaterialTheme.colorScheme.onSecondaryContainer,
                                             fontWeight = FontWeight.Bold
                                         )
                                     }
                                 }
                        },
                        bottomBox = {
                            Column {
                                standing.attending.forEachIndexed { placement, attending ->
                                    Row {
                                        Text(
                                            text = (placement+1).toString(),
                                            modifier = Modifier.weight(0.05f),
                                            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                                        )
                                        attending.team?.name?.let {
                                            Text(
                                                text = it,
                                                modifier = Modifier.weight(0.4f),
                                                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                            )
                                        }
                                        Spacer(modifier = Modifier.weight(0.2f))
                                        Text(
                                            text = attending.matches.toString(),
                                            modifier = Modifier.weight(0.075f),
                                            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                                        )
                                        Text(
                                            text = attending.wins.toString(),
                                            modifier = Modifier.weight(0.075f),
                                            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                                        )
                                        Text(
                                            text = attending.losses.toString(),
                                            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                                        )
                                    }
                                }
                            }
                        }
                    )
                }
            }
        } else Text(text = "No standings", color = MaterialTheme.colorScheme.errorContainer)

        SingleEventTopbox(
            playerOverview = playerOverview,
            teamViewModel = teamViewModel,
            statsOverview = statsOverview,
            onClickSinglePlayer = onClickSingleMatch, //TODO
            onClickSingleTeam = onClickSingleTeam,
            onClickSingleMatch = onClickSingleMatch,
            painter = countryFlag,
            recentMatches = recentMatches,
            viewModel = eventViewModel
        )
    }
}

@Composable
fun SingleEventTopbox(
    viewModel: SingleEventViewModel,
    teamViewModel: SingleTeamViewModel,
    playerOverview: SnapshotStateList<Player>,
    statsOverview: MutableState<Stats>,
    onClickSinglePlayer: (playerID: String?) -> Unit,
    onClickSingleTeam: (teamID: String?) -> Unit,
    onClickSingleMatch: (matchID: String?) -> Unit,
    painter: AsyncImagePainter,
    recentMatches: SnapshotStateList<RecentMatch>
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

                        Box {
                            Image(
                                painter = rememberAsyncImagePainter(viewModel.tournamentImage.value),
                                contentDescription = "Event logo",
                                modifier = Modifier.size(200.dp),
                                contentScale = ContentScale.Crop
                            )
                        }

                        val tournamentColors = listOf(
                            if (viewModel.palette.value?.vibrantSwatch?.rgb != null) Color(
                                viewModel.palette.value?.vibrantSwatch?.rgb!!
                            ) else Color.Black,
                            Color.White,
                        )


                        ResizingText(
                            text = buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        brush = Brush.verticalGradient(
                                            colors = tournamentColors
                                        ),
                                        fontWeight = FontWeight.ExtraBold,

                                        ),
                                ) {
                                    append(viewModel.event.value.name.toString().uppercase())
                                }
                            },
                            maxFontSize = 70.sp,
                            maxLines = 2
                        )
                        ResizingText(
                            text = buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        brush = Brush.verticalGradient(
                                            colors = tournamentColors.reversed()
                                        ),
                                        fontSize = 35.sp,
                                        fontWeight = FontWeight.Bold,
                                    ),
                                ) {
                                    append(
                                        viewModel.tournamentSeason.value.name.toString()
                                            .uppercase()
                                    )
                                }
                            },
                            modifier = Modifier.offset(y = (-20).dp),
                            maxLines = 2,
                            maxFontSize = 50.sp
                        )


                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {

                            if (viewModel.eventDetails.value.tier != null) {
                                Text(
                                    text = viewModel.eventDetails.value.tier!!.uppercase() + "-tier",
                                    color = getColorFromTier(viewModel.eventDetails.value.tier!!.uppercase()),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                            }

                            Text(
                                //textAlign = TextAlign.End,
                                text = viewModel.eventDetails.value.totalPrizeMoney.toString() + " " + viewModel.eventDetails.value.totalPrizeMoneyCurrency.toString(),
                                color = Color(parseColor("#ffbf00")),
                                style = TextStyle(
                                    shadow = Shadow(
                                        color = Color(parseColor("#bf9b30")),
                                        offset = Offset(2.0f, 2.0f),
                                        blurRadius = 2f,
                                    ),
                                    fontSize = 18.sp
                                ),
                                fontWeight = FontWeight.Bold

                            )
                        }

                        Text(
                            textAlign = TextAlign.Center,
                            text = if (!viewModel.startTime.value.contains("Unknown")) (viewModel.startTime.value + " - " + viewModel.endTime.value) else "",
                            color = Color.White
                        )

                        Divider(thickness = 1.dp)
                        Spacer(modifier = Modifier.size(4.dp))

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


                                    val gradientColors = listOf<Color>(
                                        if (teamViewModel.palette.value != null && teamViewModel.palette.value?.vibrantSwatch?.rgb != null) Color(
                                            teamViewModel.palette.value?.lightVibrantSwatch?.rgb!!
                                        ) else Color.Black,
                                        Color.White
                                    )
                                    /*
                                                                        if (teamViewModel.palette.value?.vibrantSwatch?.rgb != null) Color(
                                                                            teamViewModel.palette.value?.vibrantSwatch?.rgb!!,
                                                                        )
                                                                        else Color.Black
                                                                        */


                                    //TODO: Make this resize, possibly with resisingText? Bit hard to do while keeping it pretty
                                    Text(
                                        text = buildAnnotatedString {
                                            append("Winner\n\n")
                                            withStyle(
                                                style = SpanStyle(
                                                    brush = Brush.linearGradient(
                                                        colors = gradientColors
                                                    ),
                                                    fontSize = 65.sp,
                                                    fontWeight = FontWeight.ExtraBold,


                                                    ),
                                            ) {
                                                append(

                                                    if (teamViewModel.team.value.name != null) teamViewModel.team.value.name.toString()
                                                        .substring(
                                                            0,
                                                            minOf(
                                                                teamViewModel.team.value.name.toString().length,
                                                                15
                                                            )

                                                        ) else ""
                                                )
                                            }
                                        },
                                        maxLines = 3,
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
                                        recentMatches.forEachIndexed { index, match ->
                                            RecentMatches(
                                                modifier = Modifier.clickable {
                                                    Log.i("Ive been clicked", "Clicked left team")
                                                    onClickSingleMatch(
                                                        match.matchID.toString()
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
                                                    Log.i("Ive been clicked", "Clicked right team")
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






