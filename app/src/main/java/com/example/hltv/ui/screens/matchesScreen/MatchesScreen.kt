package com.example.hltv.ui.screens.matchesScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.hltv.data.convertTimestampToWeekDateClock
import com.example.hltv.ui.common.LiveMatchCard
import com.example.hltv.ui.common.UpcomingMatchCard
import kotlinx.coroutines.launch


@Composable
fun MatchesScreen(
    onClickSingleTeam: (String?) -> Unit,
    onClickSingleMatch: (String?) -> Unit,
    onClickSingleEvent: (String?) -> Unit
) {
    val viewModel: MatchesScreenViewModel = viewModel()
    LaunchedEffect(Unit) {
        viewModel.loadData()
    }
    val liveMatchesValues = viewModel.liveMatchesValues
    val upcomingMatchesValues = viewModel.upcomingMatchesValues
    val tournamentValues = viewModel.tournamentValues
    val loadingState by viewModel.loadingState.collectAsState()

    LazyColumn {
        items(liveMatchesValues) { item ->
            LiveMatchCard(
                modifier = Modifier.clickable { onClickSingleMatch(item.id.toString()) },
                teamOneName = item.homeTeam.name.toString(),
                teamOneIcon = rememberAsyncImagePainter(
                    viewModel.homeTeamIcons[liveMatchesValues.indexOf(
                        item
                    )]
                ),
                teamOneScore = item.homeScore!!.display!!.toInt(),
                teamOneOnClick = { onClickSingleTeam(item.homeTeam.id.toString()) },
                teamTwoName = item.awayTeam.name.toString(),
                teamTwoIcon = rememberAsyncImagePainter(
                    viewModel.awayTeamIcons[liveMatchesValues.indexOf(
                        item
                    )]
                ),
                teamTwoScore = item.awayScore!!.current!!.toInt(),
            ) { onClickSingleTeam(item.awayTeam.id.toString()) }
        }
        if (liveMatchesValues.size != 0) {
            item {
                Divider(
                    modifier = Modifier
                        .padding(top = 4.dp, bottom = 4.dp, start = 12.dp, end = 12.dp),
                    thickness = 4.dp,
                )
            }
        }
        items(upcomingMatchesValues) { item ->
            UpcomingMatchCard(
                modifier = Modifier.clickable { onClickSingleMatch(item.id.toString()) },
                teamOneName = item.homeTeam.name.toString(),
                teamOneIcon = rememberAsyncImagePainter(
                    viewModel.homeTeamIcons[liveMatchesValues.size + upcomingMatchesValues.indexOf(
                        item
                    )]
                ),
                teamOneOnClick = { onClickSingleTeam(item.homeTeam.id.toString()) },
                teamTwoName = item.awayTeam.name.toString(),
                teamTwoIcon = rememberAsyncImagePainter(
                    viewModel.awayTeamIcons[liveMatchesValues.size + upcomingMatchesValues.indexOf(
                        item
                    )]
                ),
                matchDate = convertTimestampToWeekDateClock(item.startTimestamp),
                teamTwoOnClick = { onClickSingleTeam(item.awayTeam.id.toString()) },
                tournamentIcon = rememberAsyncImagePainter(
                    viewModel.tournamentIcons[tournamentValues.indexOf(
                        item
                    )]
                ),
                tournamentOnClick = { onClickSingleEvent(item.tournament.uniqueTournament?.id.toString() + "/" + item.season.id) }
            )
        }
        item {
            AnimatedVisibility(visible = !loadingState) {  //maybe make this a loading bar instead?
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    LoadMatchesButton {
                        viewModel.viewModelScope.launch {
                            viewModel.loadUpcomingMatches()
                        }
                    }
                }
            }
            AnimatedVisibility(visible = loadingState) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.secondaryContainer)
                }
            }
        }
    }
}

@Composable
fun LoadMatchesButton(function: () -> Unit) {
    Button(
        onClick = function,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Row {
            Text(
                text = "Load more matches",
                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )

        }
    }
}

@Preview(showBackground = true)
@Composable
fun MatchesScreenPreview() {
}