package com.example.hltv.ui.screens.singleMatch

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import com.example.hltv.R
import com.example.hltv.data.remote.getMapImageFromMapID
import com.example.hltv.data.remote.getTeamImage
import com.example.hltv.ui.screens.matchesScreen.MatchesScreenViewModel

@Composable
fun SingleMatchScreen(matchID : String?, onClickSingleTeam : (String?) -> Unit){
    val viewModel : SingleMatchViewModel = viewModel()
    LaunchedEffect(Unit){
        viewModel.loadData(matchID)
        viewModel.loadGames(matchID)
    }
    val event = viewModel.event.value
    val games = viewModel.games
    Column {
        if (viewModel.LiveEvent.value != null) {
            EventImage(
                teamOneLogo = rememberAsyncImagePainter(viewModel.homeTeamIcon.value),
                teamTwoLogo = rememberAsyncImagePainter(viewModel.awayTeamIcon.value),
                teamOneScore = event?.homeScore?.display.toString(),
                teamTwoScore = event?.awayScore?.display.toString(),
                teamOneOnClick = { onClickSingleTeam(event?.homeTeam?.id.toString()) },
                teamTwoOnClick = { onClickSingleTeam(event?.awayTeam?.id.toString()) }
            )

            PredictionCard(
                teamOneIcon = rememberAsyncImagePainter(viewModel.homeTeamIcon.value),
                teamTwoIcon = rememberAsyncImagePainter(viewModel.awayTeamIcon.value),
                viewModel = viewModel, matchID = matchID
            )
        }
        else if(viewModel.UpcomingEvent.value != null){
            EventImage(
                teamOneLogo = rememberAsyncImagePainter(viewModel.homeTeamIcon.value),
                teamTwoLogo = rememberAsyncImagePainter(viewModel.awayTeamIcon.value),
                teamOneScore = event?.homeScore?.display.toString(),
                teamTwoScore = event?.awayScore?.display.toString(),
                teamOneOnClick = { onClickSingleTeam(event?.homeTeam?.id.toString()) },
                teamTwoOnClick = { onClickSingleTeam(event?.awayTeam?.id.toString()) }
            )
            PredictionCard(
                teamOneIcon = rememberAsyncImagePainter(viewModel.homeTeamIcon.value),
                teamTwoIcon = rememberAsyncImagePainter(viewModel.awayTeamIcon.value),
                viewModel = viewModel,
                matchID = matchID,
            )
        }
        else {
            EventImage(
                teamOneLogo = rememberAsyncImagePainter(viewModel.homeTeamIcon.value),
                teamTwoLogo = rememberAsyncImagePainter(viewModel.awayTeamIcon.value),
                teamOneScore = event?.homeScore?.display.toString(),
                teamTwoScore = event?.awayScore?.display.toString(),
                teamOneOnClick = { onClickSingleTeam(event?.homeTeam?.id.toString()) },
                teamTwoOnClick = { onClickSingleTeam(event?.awayTeam?.id.toString()) },
                tournamentIcon = rememberAsyncImagePainter(viewModel.tournamentIcon.value)
            )
            LazyColumn(){
                itemsIndexed(games){gameNumber, game ->
                    EventImage(
                        backgroundImage = if (gameNumber<viewModel.mapImages.size){
                            Log.i("mapImageGameNumber","$gameNumber")
                            Log.i("mapImageGameNumberImage","${viewModel.mapImages[gameNumber]}")
                            rememberAsyncImagePainter(viewModel.mapImages[gameNumber])
                        } else rememberAsyncImagePainter(R.drawable.event_background),
                        teamOneLogo = rememberAsyncImagePainter(viewModel.homeTeamIcon.value),
                        teamTwoLogo = rememberAsyncImagePainter(viewModel.awayTeamIcon.value),
                        teamOneScore = game.homeScore?.display.toString(),
                        teamTwoScore = game.awayScore?.display.toString(),
                        teamOneOnClick = { onClickSingleTeam(event?.homeTeam?.id.toString()) },
                        teamTwoOnClick = { onClickSingleTeam(event?.awayTeam?.id.toString()) }
                    )
                }
            }
        }
    }

}

@Composable
fun EventImage(
    backgroundImage: Painter = painterResource(id = R.drawable.event_background),
    tournamentIcon: Painter?=null,
    teamOneLogo: Painter,
    teamTwoLogo: Painter,
    teamOneScore: String,
    teamTwoScore: String,
    teamOneOnClick: () -> Unit,
    teamTwoOnClick: () -> Unit,
) {
    Box (modifier = Modifier.height(180.dp)){
        Image(
            painter = backgroundImage,
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .fillMaxWidth()
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = teamOneLogo,
                contentDescription = null,
                modifier = Modifier
                    .size(110.dp)
                    .align(Alignment.CenterVertically)
                    .clickable { teamOneOnClick() }
            )
            Spacer(modifier = Modifier.weight(1f))
            Column {
                Spacer(modifier = Modifier.weight(1f))
                tournamentIcon?.let {
                    Image(
                        painter = it,
                        contentDescription = null,
                        modifier = Modifier
                            .size(50.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Row {
                    Text(
                        text = teamOneScore,
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.align(alignment = Alignment.CenterVertically)
                    )
                    Text(
                        text = " - ",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    Text(
                        text = teamTwoScore,
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Live",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.Red)
                        .padding(horizontal = 12.dp, vertical = 3.dp)
                        .align(Alignment.CenterHorizontally)

                )
                Spacer(modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = teamTwoLogo,
                contentDescription = null,
                modifier = Modifier
                    .size(110.dp)
                    .align(Alignment.CenterVertically)
                    .clickable { teamTwoOnClick() }
            )
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}




@Preview(showBackground = true)
@Composable
fun SingleMatchScreenPreview() {
    //SingleMatchScreen(matchID = "1")
}