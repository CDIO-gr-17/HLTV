package com.example.hltv.ui.screens.singleEvent


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
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.hltv.R
import com.example.hltv.data.convertTimestampToDateClock
import com.example.hltv.data.getFlagFromCountryCode
import com.example.hltv.ui.common.CommonCard
import com.example.hltv.ui.screens.singleTeamScreen.Player
import com.example.hltv.ui.screens.singleTeamScreen.RecentMatch
import com.example.hltv.ui.screens.singleTeamScreen.SingleTeamViewModel
import com.example.hltv.ui.screens.singleTeamScreen.Stats
import com.example.hltv.ui.screens.singleTeamScreen.TeamOverview
import com.example.hltv.ui.screens.singleTeamScreen.overviewInfo
import com.example.hltv.ui.screens.singleTeamScreen.overviewPlayer
import com.example.hltv.ui.screens.singleTeamScreen.recentMatches
import com.example.hltv.ui.screens.singleTeamScreen.stats

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

    val recentMatches = teamViewModel.recentMatches
    val playerOverview = teamViewModel.playerOverview
    val statsOverview = teamViewModel.statisticsOverview
    val countryCode = statsOverview.value.countryCode
    val countryFlag = getFlagFromCountryCode(countryCode = countryCode)


    SingleEventTopbox(playerOverview = playerOverview,
        statsOverview = statsOverview,
        onClickSinglePlayer = {Log.i("SingleEventScreen", "Clicked onClickSinglePlayer")},
        onClickSingleTeam = {Log.i("SingleEventScreen", "Clicked onClickSingleTeam")},
        onClickSingleMatch = {Log.i("SingleEventScreen", "Clicked onClickSingleMatch")},
        painter = countryFlag,
        recentMatches = recentMatches,
        viewModel = eventViewModel
        )




}

@Composable
fun SingleEventTopbox(viewModel: SingleEventViewModel,
                      playerOverview : SnapshotStateList<Player>,
                      statsOverview : MutableState<Stats>,
                      onClickSinglePlayer: (String?) -> Unit,
                      onClickSingleTeam: (String?) -> Unit,
                      onClickSingleMatch: (String?) -> Unit,
                      painter : AsyncImagePainter,
                      recentMatches : SnapshotStateList<RecentMatch>
){

    LazyColumn{
        item{CommonCard (
            modifier = Modifier,
            customInnerPadding = 0.dp,
            customOuterPadding = 0.dp,
            topBox = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Image(rememberAsyncImagePainter(model = R.drawable.person_24px/*viewModel.tournamentImage.value*/),
                        contentDescription = "Team logo",
                        modifier = Modifier.size(70.dp))

                    Text( //Tournament name
                        textAlign = TextAlign.Center,
                        text = "${viewModel.event.value.name} ${viewModel.tournamentSeason.value.name}",
                        color = Color.White
                    )

                    Text(
                        textAlign = TextAlign.Center,
                        text = viewModel.eventDetails.value.totalPrizeMoney.toString() + " " + viewModel.eventDetails.value.totalPrizeMoneyCurrency.toString(),
                        color = Color.White
                    )
                    Text(
                        textAlign = TextAlign.Center,
                        text = (viewModel.startTime.value + " - " + viewModel.endTime.value),
                        color = Color.White
                    )
                }
            },
            bottomBox = {
                Box{
                    Column(
                        //horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ){

                        //COPY PASTE FROM SINGLETEAM
                        //I tried doing DI, but we cant because the lazycolumn needs an argument
                        //that you cant pass so wed need DI in the DI and that was too much work

                        LazyColumn (modifier = Modifier.fillParentMaxHeight()) {

                            item {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                )
                                {
                                    val iconSize = 30.dp
                                    Spacer( //Ghetto but it puts it in the middle
                                        modifier = Modifier.size(iconSize)
                                    )
                                    Text(
                                        fontSize = 30.sp,
                                        modifier = Modifier,
                                        text = "Winner"
                                    )

                                    Image(
                                        modifier = Modifier.size(iconSize),
                                        painter = painterResource(id = R.drawable.diversity_3_24px),
                                        contentDescription = "Cup"
                                    )
                                }
                            }

                            item{
                                Divider(
                                    color = Color.Black, //TODO: Decide on this
                                    thickness = 1.dp
                                )
                            }

                            item{
                                Image(
                                    modifier = Modifier.size(100.dp).align(Alignment.CenterHorizontally),
                                    painter = painterResource(R.drawable.astralis_logo),
                                    contentDescription = "Team logo",

                                )
                            }




                            item {
                                CommonCard(modifier = Modifier, bottomBox = {
                                    Column {
                                        LazyRow{
                                            items(playerOverview.size){ index ->
                                                overviewPlayer(
                                                    player = playerOverview[index],
                                                    onClickSinglePlayer
                                                )
                                            }
                                        }
                                        overviewInfo(
                                            country = statsOverview.value.countryName,
                                            countryImage = painter,
                                            worldRank = "#"
                                        )
                                        stats(
                                            coach = "Peter 'Castle' Ardenskjold",
                                            points = "1000",
                                            winRate = "61%",
                                            bestMap = "Overpass",
                                            averagePlayerAge = statsOverview.value.avgAgeofPlayers,
                                            imageNat = painterResource(R.drawable.dk_flag)
                                        )
                                        Text(text = "Recent Matches",
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                        LazyColumn (Modifier.fillParentMaxHeight()) {
                                            items(recentMatches.size) { index ->
                                                recentMatches(
                                                    modifier = Modifier.clickable { onClickSingleMatch(recentMatches[index].matchID.toString()) },
                                                    team1 = recentMatches[index].homeTeam?.name,
                                                    team2 = recentMatches[index].awayTeam?.name,
                                                    imageTeam1 = rememberAsyncImagePainter(recentMatches[index].homeTeamImage),
                                                    imageTeam2 = rememberAsyncImagePainter(recentMatches[index].awayTeamImage),
                                                    team2OnClick = { onClickSingleTeam(recentMatches[index].awayTeam?.id.toString()) },
                                                    score = recentMatches[index].homeScore?.display.toString() + " - " + recentMatches[index].awayScore?.display.toString(),
                                                    date = recentMatches[index].startTimestamp.toString()
                                                )
                                            }
                                        }
                                    }
                                })
                            }
                        }

                        ///END OF COPY PASTE

                    }
                }
            }
        )
    }
}


    /*

    CommonCard( modifier = Modifier,
        bottomBox = {
            Box(contentAlignment = Alignment.CenterEnd){
                Row(modifier = Modifier.align(Alignment)){
                    Text(
                        textAlign = TextAlign.Center,
                        text = "Horse",
                        color = Color.White
                    )
                }

            }

        }
    )

     */

}


