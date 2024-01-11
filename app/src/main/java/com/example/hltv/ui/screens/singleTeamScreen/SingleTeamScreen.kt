package com.example.hltv.ui.screens.singleTeamScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.hltv.R
import com.example.hltv.data.getFlagFromCountryCode
import com.example.hltv.ui.common.CommonCard

@Composable
fun SingleTeamScreen(teamID : String? = "364378", onClickSinglePlayer: (String?) -> Unit, onClickSingleTeam: (String?) -> Unit, onClickSingleMatch: (String?) -> Unit){
    val viewModel : SingleTeamViewModel = viewModel()
    LaunchedEffect(teamID){
        viewModel.loadData(teamID!!)
    }
    val recentMatches = viewModel.recentMatches
    val playerOverview = viewModel.playerOverview
    val statsOverview = viewModel.statisticsOverview
    val countryCode = statsOverview.value.countryCode
    val painter = getFlagFromCountryCode(countryCode = countryCode)


    TeamOverview(
        playerOverview = playerOverview,
        statsOverview = statsOverview,
        onClickSinglePlayer = onClickSinglePlayer,
        onClickSingleTeam = onClickSingleTeam,
        onClickSingleMatch = onClickSingleTeam,
        painter = painter,
        recentMatches = recentMatches
    )
}


@Composable
fun TeamOverview(
    playerOverview : SnapshotStateList<Player>,
    statsOverview : MutableState<Stats>,
    onClickSinglePlayer: (String?) -> Unit,
    onClickSingleTeam: (String?) -> Unit,
    onClickSingleMatch: (String?) -> Unit,
    painter : AsyncImagePainter,
    recentMatches : SnapshotStateList<RecentMatch>
    ){
    LazyColumn {
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
}






// Lige nu er der 'gap' mellem hver spiller. Det skal fjernes. Evt. Equal Weight i stedet for SpaceEvenly?
@Composable
fun overviewPlayers(
    players: List<Player>,
    onClickSinglePlayer: (String?) -> Unit) {
    Row (
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ){
        for (player in players) {
            overviewPlayer(player = player, onClickSinglePlayer)
        }
    }
}

@Composable
fun overviewPlayer(
    player: Player,
    onClickSinglePlayer: (String?) -> Unit
){
    Row {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .width(70.dp)
                .clickable {
                    onClickSinglePlayer(player.playerId.toString())
                }
        ){
            Image(
                painter = if(player.image!=null) rememberAsyncImagePainter(player.image) else rememberAsyncImagePainter(
                    model = R.drawable.person_24px
                ),
                contentDescription = null,
                alignment = Alignment.CenterStart,
                modifier = Modifier
                    .size(70.dp)
                    .offset(y = 20.dp)
            )
            CommonCard (modifier = Modifier,
                customOuterPadding = 0.dp,
                topBox = {
                    Text(
                        text = if (player.name!=null) player.name.substring(0, minOf(6, player.name.length)) else "Player",
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Visible //TODO: Do we want ellipses
                    )
                }
            )
        }
    }
}

@Composable
fun overviewInfo(
    country: String?,
    countryImage: Painter,
    worldRank: String
){
    Row (
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                text = country ?: "Unknown",
                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontWeight = FontWeight.Bold
            )
            Image(
                painter = countryImage,
                contentDescription = null,
                alignment = Alignment.CenterStart,
                modifier = Modifier
                    .size(40.dp)
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                text = "World Ranking",
                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "#" + worldRank,
                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
fun recentMatches(
    modifier: Modifier = Modifier,
    team1: String ?= null,
    team2: String ?= null,
    imageTeam1: Painter,
    imageTeam2: Painter,
    team2OnClick: () -> Unit,
    score: String ?= null,
    date: String ?= null,
    matchID: Int ?= null
){
    CommonCard(
        modifier = Modifier.fillMaxWidth(),
        topBox = {
            Box {
                Row (
                    modifier = modifier
                        .fillMaxWidth()
                ){
                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(0.4f)
                    ){
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
                            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    Column (
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(0.2f)
                    ){
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
                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier
                            .weight(0.4f)
                            .clickable { team2OnClick() }
                    ){
                        Text(
                            text = team2 ?: "Team 2",
                            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
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
fun stats(
    coach: String,
    points: String,
    winRate: String,
    bestMap: String,
    averagePlayerAge: Double?,
    imageNat: Painter){

            Box{
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Column (
                        horizontalAlignment = Alignment.Start
                    ){
                        Text(text = "Statistics",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "Coach:",
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "Points:",
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "Win Rate:",
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "Best Map:",
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "Average Player Age:",
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    Column (
                        horizontalAlignment = Alignment.End
                    ){
                        Text(text = "")
                        Row (verticalAlignment = Alignment.CenterVertically
                        ){
                            Text(
                                text = coach,
                                color = MaterialTheme.colorScheme.onPrimaryContainer)
                            Image(
                                painter = imageNat,
                                contentDescription = null,
                                alignment = Alignment.CenterEnd,
                                modifier = Modifier
                                    .size(20.dp)
                                    .padding(1.dp)
                                )
                        }
                        Text(
                            text = points,
                            color = MaterialTheme.colorScheme.onPrimaryContainer)
                        Text(
                            text = winRate,
                            color = MaterialTheme.colorScheme.onPrimaryContainer)
                        Text(
                            text = bestMap,
                            color = MaterialTheme.colorScheme.onPrimaryContainer)
                        Text(
                            text = averagePlayerAge.toString().substring(0,minOf(4, averagePlayerAge.toString().length)),
                            color = MaterialTheme.colorScheme.onPrimaryContainer)
                    }
                }
            }
        }



@Composable
@Preview
fun SingleTeamPreview(){
    //SingleTeam()
}