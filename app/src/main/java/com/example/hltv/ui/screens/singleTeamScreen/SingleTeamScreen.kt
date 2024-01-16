package com.example.hltv.ui.screens.singleTeamScreen

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.hltv.R
import com.example.hltv.data.getFlagFromCountryCode
import com.example.hltv.ui.common.CommonCard
import com.example.hltv.ui.common.showToast
import com.example.hltv.ui.screens.homeScreen.oldTeamID
import java.text.DecimalFormat

@Composable
fun SingleTeamScreen(
    teamID: String?,
    onClickSinglePlayer: (String?) -> Unit,
    onClickSingleTeam: (String?) -> Unit,
    onClickSingleMatch: (String?) -> Unit,
    calledFromHomeScreen: Boolean = false
) {
    LazyColumn {
        item {
            SingleTeamScreenComposable(
                teamID,
                onClickSinglePlayer,
                onClickSingleTeam,
                onClickSingleMatch,
                calledFromHomeScreen,
 oldTeamID
            )
        }
    }
}

@Composable
fun SingleTeamScreenComposable(
    teamID: String?,
    onClickSinglePlayer: (String?) -> Unit,
    onClickSingleTeam: (String?) -> Unit,
    onClickSingleMatch: (String?) -> Unit,
    calledFromHomeScreen: Boolean = false,
    oldTeamID: Int = 1
) {
    val viewModel: SingleTeamViewModel = viewModel()
    val gamesToLoad = if (calledFromHomeScreen) 3 else 10
    Log.i("SingleTeamScreenComposable", "Over here: " + oldTeamID.toString() + " vs "+ teamID)
    LaunchedEffect(teamID) {
        Log.i("SingleTeamScreenComposable", oldTeamID.toString() + " vs " + teamID)
        if (calledFromHomeScreen && oldTeamID.toString() != teamID){
            viewModel.dataLoaded = false
            viewModel.loadData(teamID!!, gamesToLoad = gamesToLoad)
        } else {
            viewModel.loadData(teamID!!, gamesToLoad = gamesToLoad)
        }
    }
    val recentMatches = viewModel.recentMatches
    val playerOverview = viewModel.playerOverview
    val statsOverview = viewModel.statisticsOverview
    val countryCode = statsOverview.value.countryCode
    val painter = getFlagFromCountryCode(countryCode = countryCode)
    val winRate = viewModel.winRate.collectAsState()

    if (viewModel.noInfoOnTeam.value) {
        showToast(message = "No info on this team", time = 5)
    }

    Column(
        modifier = Modifier
    ) {
        CommonCard(modifier = Modifier,
            topBox = {
                if (calledFromHomeScreen) {
                    viewModel.team.value.name?.let {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onClickSingleTeam(teamID) },
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = "Favorite Icon",
                                tint = Color(0xffE91E63)
                            )
                            Text(
                                text = " " + it+ " ",
                                fontSize = 20.sp,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                            )
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = "Favorite Icon",
                                tint =  Color(0xffE91E63)
                            )

                        }

                    }
                } else null
            },
            bottomBox = {
                Column {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        items(playerOverview.size) { index ->
                            OverviewPlayer(
                                player = playerOverview[index],
                                onClickSinglePlayer
                            )
                        }
                    }
                    OverviewInfo(
                        country = statsOverview.value.countryName,
                        countryImage = painter,
                        teamLogo = recentMatches.getOrNull(0)?.homeTeamImage?.let {
                            rememberAsyncImagePainter(
                                it
                            )
                        }

                    )
                    Statistics(
                        winRate = winRate.value,
                        averagePlayerAge = statsOverview.value.avgAgeOfPlayers,
                    )
                    Spacer(modifier = Modifier.size(15.dp))
                    Text(
                        text = "Recent Matches",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    recentMatches.forEach { recentMatch ->
                        RecentMatches(
                            modifier = Modifier.clickable { onClickSingleMatch(recentMatch.matchID.toString()) },
                            team1 = recentMatch.homeTeam?.name,
                            team2 = recentMatch.awayTeam?.name,
                            imageTeam1 = rememberAsyncImagePainter(recentMatch.homeTeamImage),
                            imageTeam2 = rememberAsyncImagePainter(recentMatch.awayTeamImage),
                            team2OnClick = { onClickSingleTeam(recentMatch.awayTeam?.id.toString()) },
                            score = recentMatch.homeScore?.display.toString() + " - " + recentMatch.awayScore?.display.toString(),
                            date = recentMatch.startTimestamp.toString()
                        )
                    }
                }
            })

    }
}

@Composable
fun OverviewPlayer(
    player: Player,
    onClickSinglePlayer: (String?) -> Unit
) {
    Row(modifier = Modifier.height(100.dp)) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .width(70.dp)
                .clickable {
                    onClickSinglePlayer(player.playerId.toString())
                }
        ) {
            Image(
                painter = if (player.image != null) rememberAsyncImagePainter(player.image) else rememberAsyncImagePainter(
                    model = R.drawable.playersilouhette
                ),
                contentDescription = null,
                alignment = Alignment.CenterStart,
                modifier = Modifier
                    .size(70.dp)
            )
            CommonCard(modifier = Modifier.offset(y = 40.dp),
                customOuterPadding = 0.dp,
                topBox = {
                    Text(
                        text = if (player.name != null) player.name.substring(
                            0,
                            minOf(6, player.name.length)
                        ) else "Player",
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Visible
                    )
                }
            )
        }
    }
}

@Composable
fun OverviewInfo(
    country: String?,
    countryImage: Painter,
    teamLogo: Painter? = null,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = country ?: "Unknown",
                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontWeight = FontWeight.Bold
            )
            Image(
                painter = countryImage,
                contentDescription = null,
                alignment = Alignment.Center,
                modifier = Modifier
                    .size(40.dp)
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (teamLogo != null) {
                Image(
                    painter = teamLogo,
                    contentDescription = null,
                    alignment = Alignment.Center,
                    modifier = Modifier
                        .size(50.dp)
                )
            }
        }
    }
}

@Composable
fun RecentMatches(
    modifier: Modifier = Modifier,
    team1: String? = null,
    team2: String? = null,
    imageTeam1: Painter,
    imageTeam2: Painter,
    team2OnClick: () -> Unit,
    score: String? = null,
    date: String? = null,
) {
    CommonCard(
        modifier = Modifier.fillMaxWidth(),
        topBox = {
            Box {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = modifier
                        .fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(0.4f)
                    ) {
                        Image(
                            painter = imageTeam1,
                            contentDescription = null,
                            alignment = Alignment.CenterStart,
                            modifier = Modifier
                                .size(40.dp)
                                .padding(end = 4.dp)
                        )
                        Text(
                            text = team1 ?: "Team 1",
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(0.2f)
                    ) {
                        Text(
                            text = score ?: "/",
                            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = date ?: "-",
                            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier
                            .weight(0.4f)
                            .clickable { team2OnClick() }
                    ) {
                        Text(
                            text = team2 ?: "Team 2",
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            textAlign = TextAlign.End,
                            modifier = Modifier.weight(1f)
                        )
                        Image(
                            painter = imageTeam2,
                            contentDescription = null,
                            alignment = Alignment.CenterEnd,
                            modifier = Modifier
                                .size(40.dp)
                                .padding(start = 4.dp)
                        )
                    }
                }
            }
        }
    )
}


@Composable
fun Statistics(
    winRate: Double? = null,
    averagePlayerAge: Double?,
) {
    if (winRate != 0.0 || averagePlayerAge != null) {
        Box {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Statistics",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "Win Rate Recent Matches:",
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    if (averagePlayerAge != null && averagePlayerAge != 0.0) {
                        Text(
                            text = "Average Player Age:",
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(text = "")
                    Text(
                        text = DecimalFormat("#.#").format(winRate) + "%",
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    if (averagePlayerAge != null && averagePlayerAge != 0.0) {
                        Text(
                            text = DecimalFormat("#.#").format(averagePlayerAge),
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        }
    }
}


@Composable
@Preview
fun SingleTeamPreview() {
    //SingleTeamScreen(onClickSinglePlayer = unit, onClickSingleTeam = item, onClickSingleMatch = )
}