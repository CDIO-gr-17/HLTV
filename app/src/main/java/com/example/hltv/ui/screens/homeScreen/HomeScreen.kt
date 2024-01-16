@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.hltv.ui.screens.homeScreen

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.hltv.R
import com.example.hltv.data.convertTimestampToWeekDateClock
import com.example.hltv.data.local.PrefDataKeyValueStore
import com.example.hltv.ui.common.CommonCard
import com.example.hltv.ui.common.LiveMatchCard
import com.example.hltv.ui.common.UpcomingMatchCard
import com.example.hltv.ui.screens.singleTeamScreen.SingleTeamScreenComposable

val M = MaterialTheme

@Composable
fun HomeScreen(
    onClickSingleTeam: (String?) -> Unit,
    onClickSingleMatch: (String?) -> Unit,
    onClickSingleEvent: (String?) -> Unit,
    onClickSinglePlayer: (String?) -> Unit
) {
    val viewModel: HomeScreenViewModel = viewModel()
    val dataStore = PrefDataKeyValueStore.getInstance(LocalContext.current)
    val favoritteamID by viewModel.favoriteTeam.collectAsState()
    viewModel.loadFavoriteTeam(dataStore)

    LaunchedEffect(Unit) {
        viewModel.loadData()
    }



    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .testTag("HomeScreen")
    ) {
        item {
            if (viewModel.liveMatchValue.value != null) {
                LiveMatchCard(
                    title = "Highlighted match",
                    modifier = Modifier.clickable { onClickSingleMatch(viewModel.liveMatchValue.value!!.id.toString()) },
                    teamOneName = viewModel.liveMatchValue.value!!.homeTeam.name ?: "Unknown",
                    teamOneIcon = rememberAsyncImagePainter(model = viewModel.homeTeamIcon.value),
                    teamOneScore = viewModel.liveMatchValue.value!!.homeScore?.current ?: 0,
                    teamOneOnClick = { onClickSingleTeam(viewModel.liveMatchValue.value!!.homeTeam.id.toString()) },
                    teamTwoName = viewModel.liveMatchValue.value!!.awayTeam.name ?: "Unknown",
                    teamTwoIcon = rememberAsyncImagePainter(model = viewModel.awayTeamIcon.value),
                    teamTwoScore = viewModel.liveMatchValue.value!!.awayScore?.current ?: 0,
                    teamTwoOnClick = { onClickSingleTeam(viewModel.liveMatchValue.value!!.awayTeam.id.toString()) },
                )
            } else if (viewModel.upcomingMatchValue.value != null) {
                UpcomingMatchCard(
                    modifier = Modifier.clickable { onClickSingleMatch(viewModel.upcomingMatchValue.value!!.id.toString()) },
                    teamOneName = viewModel.upcomingMatchValue.value!!.homeTeam.name ?: "Unknown",
                    teamOneIcon = rememberAsyncImagePainter(model = viewModel.homeTeamIcon.value),
                    teamOneOnClick = { onClickSingleTeam(viewModel.upcomingMatchValue.value!!.homeTeam.id.toString()) },
                    teamTwoName = viewModel.upcomingMatchValue.value!!.awayTeam.name ?: "Unknown",
                    teamTwoIcon = rememberAsyncImagePainter(model = viewModel.awayTeamIcon.value),
                    teamTwoOnClick = { onClickSingleTeam(viewModel.upcomingMatchValue.value!!.awayTeam.id.toString()) },
                    matchDate = convertTimestampToWeekDateClock(viewModel.upcomingMatchValue.value!!.startTimestamp),
                    tournamentIcon = rememberAsyncImagePainter(model = viewModel.tournamentIcon.value),
                    tournamentOnClick = { onClickSingleEvent(viewModel.upcomingMatchValue.value!!.tournament.uniqueTournament?.id.toString() + "/" + viewModel.upcomingMatchValue.value!!.season.id) }
                )

            }
        }
        item {
            Divider(
                modifier = Modifier.padding(horizontal = 8.dp),
                color = M.colorScheme.onBackground
            )
        }
        /*if (viewModel.upcomingTournament.value != null) {

            SingleEventCard(
                eventTitle = viewModel.upcomingTournament.value!!.name.toString(),
                eventDate = convertTimestampToDateDisplay(viewModel.upcomingTournament.value!!.startDateTimestamp),
                eventLogo = rememberAsyncImagePainter(model = viewModel.upcomingTournamentlogo.value),
                tier = "No Tier",
                /*if (viewModel.uniqueTournament.value!!.uniqueTournamentInfo.tier != null)
                    viewModel.uniqueTournament.value!!.uniqueTournamentInfo.tier?.uppercase() else "Not Tier",
                prizePool = viewModel.uniqueTournament.value!!.uniqueTournamentInfo.totalPrizeMoney,
                competitors = viewModel.uniqueTournament.value!!.uniqueTournamentInfo.numberOfCompetitors,
                prizePoolCurrency = viewModel.uniqueTournament.value!!.uniqueTournamentInfo.totalPrizeMoneyCurrency,*/
                modifier = Modifier.clickable { onClickSingleEvent(viewModel.upcomingTournament.value!!.id.toString()) },
            )
        }*/
        item {
            Divider(
                modifier = Modifier.padding(horizontal = 8.dp),
                color = M.colorScheme.onBackground
            )
        }
        item {
            if (favoritteamID != 0 && favoritteamID != null) {
                SingleTeamScreenComposable(
                    teamID = favoritteamID.toString(),
                    onClickSinglePlayer = onClickSinglePlayer,
                    onClickSingleTeam = onClickSingleTeam,
                    onClickSingleMatch = onClickSingleMatch,
                    calledFromHomeScreen = true
                )
            } else {
                val stdBitmap = BitmapFactory.decodeResource(
                    LocalContext.current.resources, R.drawable.questionmark
                ).asImageBitmap()
                CommonCard(
                    modifier = Modifier,
                    topBox = {
                        Text(text = "No favorite team selected",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth())
                    },
                    bottomBox = {
                        Column(horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()) {
                            Text(text = "Your favorite team will be shown here",
                                textAlign = TextAlign.Center,)
                            Image(
                                modifier = Modifier
                                    .size(80.dp),
                                bitmap = stdBitmap,
                                contentDescription = "No favorite team selected",
                                alignment = Alignment.Center
                            )
                        }

                    }
                )

            }
        }
    }
}


@Preview
@Composable
fun HomeScreenPreview() {
    //HomeScreen()
}