package com.example.hltv.ui.screens.singleTeam

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hltv.R
import com.example.hltv.ui.common.CommonCard
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

data class Player(val name: String ?= null, val image: Painter ?= null)
@Composable
fun SingleTeam(){
    LazyColumn {
        item {
            CommonCard(modifier = Modifier, bottomBox = {
                Column {
                    overviewPlayers(
                        players = listOf(
                            Player(name = null, image = null),
                            Player("b0RUP", image = null),
                            Player("blameF", image = null),
                            Player("Staehr", image = null),
                            Player("Buzz", image = null)
                        )
                    )
                    overviewInfo(
                        country = "Denmark",
                        countryImage = painterResource(id = R.drawable.dk_flag),
                        worldRank = "5"
                    )
                    recentMatches(
                        team1 = "Astralis",
                        team2 = "Astralis",
                        imageTeam1 = painterResource(id = R.drawable.astralis_logo),
                        imageTeam2 = painterResource(id = R.drawable.astralis_logo),
                        score = "16-10",
                        date = "10 October"
                    )
                    stats(
                        coach = "Peter 'Castle' Ardenskjold",
                        points = "1000",
                        winRate = "61%",
                        bestMap = "Overpass",
                        averagePlayerAge = "25",
                        imageNat = painterResource(R.drawable.dk_flag)
                    )
                }
            })
        }
    }
}


// Lige nu er der 'gap' mellem hver spiller. Det skal fjernes. Evt. Equal Weight i stedet for SpaceEvenly?
@Composable
fun overviewPlayers(players: List<Player>) {
    Row (
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ){
        for (player in players) {
            overviewPlayer(player = player)
        }
    }
}

@Composable
fun overviewPlayer(
    player: Player
){
    Row {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Image(
                painter = player.image ?: painterResource(id = R.drawable.person_24px),
                contentDescription = null,
                alignment = Alignment.CenterStart,
                modifier = Modifier
                    .size(70.dp)
                    .offset(y = 20.dp)
            )
            CommonCard (modifier = Modifier.width(IntrinsicSize.Min),
                customOuterPadding = 0.dp,
                topBox = {
                    Text(
                        text = player.name ?: "Unknown",
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        color = MaterialTheme.colorScheme.onPrimary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        }
    }
}

@Composable
fun overviewInfo(
    country: String,
    countryImage: Painter,
    worldRank: String
){
    Row (
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                text = country,
                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                color = MaterialTheme.colorScheme.onPrimary,
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
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "#" + worldRank,
                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
fun recentMatches(
    team1: String,
    team2: String,
    imageTeam1: Painter,
    imageTeam2: Painter,
    score: String,
    date: String,

){
    CommonCard(
        modifier = Modifier.fillMaxWidth(),
        topBox = {
            Box {
                Row (
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Row (
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Image(
                            painter = imageTeam1,
                            contentDescription = null,
                            alignment = Alignment.CenterStart,
                            modifier = Modifier
                                .size(40.dp)
                        )
                        Text(
                            text = team1,
                            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    Column (
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Text(
                            text = score,
                            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = date,
                            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    Row (
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(
                            text = team2,
                            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Image(
                            painter = imageTeam2,
                            contentDescription = null,
                            alignment = Alignment.CenterEnd,
                            modifier = Modifier
                                .size(40.dp)
                        )
                    }
                }
            }
        }
    )
}


@Composable
fun stats(coach: String,
          points: String,
          winRate: String,
          bestMap: String,
          averagePlayerAge: String,
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
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Text(
                            text = "Coach:",
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Text(
                            text = "Points:",
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Text(
                            text = "Win Rate:",
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Text(
                            text = "Best Map:",
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Text(
                            text = "Average Player Age:",
                            color = MaterialTheme.colorScheme.onPrimary
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
                                color = MaterialTheme.colorScheme.onPrimary)
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
                            color = MaterialTheme.colorScheme.onPrimary)
                        Text(
                            text = winRate,
                            color = MaterialTheme.colorScheme.onPrimary)
                        Text(
                            text = bestMap,
                            color = MaterialTheme.colorScheme.onPrimary)
                        Text(
                            text = averagePlayerAge,
                            color = MaterialTheme.colorScheme.onPrimary)
                    }
                }
            }
        }



@Composable
@Preview
fun SingleTeamPreview(){
    SingleTeam()
}