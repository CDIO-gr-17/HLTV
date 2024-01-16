package com.example.hltv.ui.screens.singleEvent


import android.graphics.Color.parseColor
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.hltv.data.getColorFromTier
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
    onClickSingleMatch: (matchID: String?) -> Unit,
    onClickSinglePlayer: (playerID: String?) -> Unit
) {
    Log.i("SingleEventScreen", "TournamentID: $tournamentID")
    Log.i("SingleEventScreen", "seasonID: $seasonID")

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

    val recentMatches = teamViewModel.recentMatches
    val playerOverview = teamViewModel.playerOverview
    val statsOverview = teamViewModel.statisticsOverview
    val standings = eventViewModel.standings

    LazyColumn {
        item {
            SingleEventBox(
                playerOverview = playerOverview,
                teamViewModel = teamViewModel,
                statsOverview = statsOverview,
                onClickSinglePlayer = onClickSinglePlayer,
                onClickSingleTeam = onClickSingleTeam,
                onClickSingleMatch = onClickSingleMatch,
                recentMatches = recentMatches,
                eventViewModel = eventViewModel,
                winRate = teamViewModel.winRate.collectAsState().value
            )
        }
        item {
            if (standings.isNotEmpty()) {
                standings.reversed().forEach { standing ->
                    Column {
                        CommonCard(modifier = Modifier, topBox = {
                            Column {
                                Row(modifier = Modifier.fillMaxWidth()) {
                                    Text(
                                        text = if (standings.size != 0) "Standings ${
                                            standing.name?.removePrefix(
                                                standing.tournament?.uniqueTournament?.name.toString() + " "
                                            )
                                                ?.removeSuffix(standing.tournament?.uniqueTournament?.name.toString() + " ")
                                        }" else "Standings",
                                        fontSize = 18.sp,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Row(modifier = Modifier.fillMaxWidth()) {
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
                        }) {
                            Column {
                                standing.attending.forEachIndexed { placement, attending ->
                                    Row {
                                        Text(
                                            text = (placement + 1).toString(),
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
                    }
                }
            } else Log.i("SingleEvent", "There were no standings for the event")
        }
    }
}

@Composable
fun SingleEventBox(
    eventViewModel: SingleEventViewModel,
    teamViewModel: SingleTeamViewModel,
    playerOverview: SnapshotStateList<Player>,
    statsOverview: MutableState<Stats>,
    onClickSinglePlayer: (playerID: String?) -> Unit,
    onClickSingleTeam: (teamID: String?) -> Unit,
    onClickSingleMatch: (matchID: String?) -> Unit,
    recentMatches: SnapshotStateList<RecentMatch>,
    winRate: Double,
) {

    Column {
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

                    Image(
                        painter = rememberAsyncImagePainter(eventViewModel.tournamentImage.value),
                        contentDescription = "Event logo",
                        modifier = Modifier.size(200.dp),
                        contentScale = ContentScale.Crop
                    )
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        ResizingText(
                            text = buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        brush = Brush.verticalGradient(
                                            colors = listOf(
                                                MaterialTheme.colorScheme.onSecondaryContainer,
                                                eventViewModel.color.value
                                            )
                                        ),
                                        fontWeight = FontWeight.ExtraBold,

                                        ),
                                ) {
                                    append(eventViewModel.event.value.name.toString().uppercase())
                                }
                            }, maxFontSize = 70.sp, maxLines = 2
                        )
                        ResizingText(
                            text = buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        brush = Brush.verticalGradient(
                                            colors = listOf(
                                                eventViewModel.color.value,
                                                MaterialTheme.colorScheme.onSecondaryContainer
                                            )
                                        ),
                                        fontSize = 35.sp,
                                        fontWeight = FontWeight.Bold,
                                    ),
                                ) {
                                    append(
                                        eventViewModel.tournamentSeason.value.name.toString()
                                            .uppercase()
                                    )
                                }
                            },
                            modifier = Modifier.offset(y = (-20).dp),
                            maxLines = 2,
                            maxFontSize = 50.sp
                        )
                    }



                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {

                        if (eventViewModel.eventDetails.value.tier != null) {
                            Text(
                                text = eventViewModel.eventDetails.value.tier!!.uppercase() + "-tier",
                                color = getColorFromTier(eventViewModel.eventDetails.value.tier!!.uppercase()),
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp
                            )
                            if (eventViewModel.eventDetails.value.totalPrizeMoney != null) {
                                Text(
                                    textAlign = TextAlign.End, //This doesn't do anything, the lord knows why
                                    text = eventViewModel.eventDetails.value.totalPrizeMoney.toString() + " " + eventViewModel.eventDetails.value.totalPrizeMoneyCurrency.toString(),
                                    color = Color(parseColor("#ffbf00")),
                                    style = TextStyle(
                                        shadow = Shadow(
                                            color = Color(parseColor("#bf9b30")),
                                            offset = Offset(2.0f, 2.0f),
                                            blurRadius = 2f,
                                        ), fontSize = 22.sp
                                    ),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        } else if (eventViewModel.event.value.name == null) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                CircularProgressIndicator()
                            }

                            /*
                            Text(
                                text = "Loading...",
                                color = getColorFromTier("Placeholder input"),
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp
                            )

                             */
                        }


                    }

                    Text(
                        textAlign = TextAlign.Center,
                        text = if (!eventViewModel.startTime.value.contains("Unknown")) (eventViewModel.startTime.value + " - " + eventViewModel.endTime.value) else "",
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )

                    Divider(thickness = 1.dp)
                    Spacer(modifier = Modifier.size(4.dp))

                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (teamViewModel.team.value.id != null) {
                            Column(modifier = Modifier.clickable {
                                onClickSingleTeam(
                                    teamViewModel.team.value.id.toString()
                                )
                            }) {

                                CommonCard(modifier = Modifier) {
                                    Column {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                        ) {


                                            Image(
                                                rememberAsyncImagePainter(
                                                    teamViewModel.teamImage.value
                                                ),
                                                contentDescription = teamViewModel.team.value.name,
                                                Modifier.size(69.dp)
                                            )

                                            Text(
                                                text = buildAnnotatedString {
                                                    append("Winner\n\n")
                                                    withStyle(
                                                        style = SpanStyle(
                                                            brush = Brush.linearGradient(
                                                                colors = listOf(
                                                                    teamViewModel.color.value,
                                                                    MaterialTheme.colorScheme.onSecondaryContainer
                                                                )
                                                            ),
                                                            fontSize = 65.sp,
                                                            fontWeight = FontWeight.ExtraBold
                                                            ),
                                                    ) {
                                                        append(
                                                            if (teamViewModel.team.value.name != null) teamViewModel.team.value.name.toString()
                                                                .substring(
                                                                    0, minOf(
                                                                        teamViewModel.team.value.name.toString().length,
                                                                        8
                                                                    )
                                                                ) else ""
                                                        )
                                                    }
                                                },
                                                maxLines = 3,
                                            )
                                        }
                                        LazyRow {
                                            items(playerOverview.size) { index ->
                                                OverviewPlayer(
                                                    player = playerOverview[index],
                                                    onClickSinglePlayer = onClickSinglePlayer
                                                )
                                            }
                                        }
                                        Statistics(
                                            winRate = winRate,
                                            averagePlayerAge = statsOverview.value.avgAgeOfPlayers,
                                        )
                                        Spacer(modifier = Modifier.size(15.dp))
                                        Text(
                                            text = "Recent Matches",
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                        recentMatches.forEachIndexed { index, match ->
                                            RecentMatches(
                                                modifier = Modifier.clickable {
                                                    Log.i(
                                                        "Ive been clicked", "Clicked left team"
                                                    )
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
                                                    Log.i(
                                                        "Ive been clicked", "Clicked right team"
                                                    )
                                                    onClickSingleTeam(
                                                        recentMatches[index].awayTeam?.id.toString()
                                                    )
                                                },
                                                score = recentMatches[index].homeScore?.display.toString() + " - " + recentMatches[index].awayScore?.display.toString(),
                                                date = recentMatches[index].startTimestamp.toString()
                                            )
                                        }

                                    }
                                }
                            }
                        }
                    }
                }
            })
    }
}






