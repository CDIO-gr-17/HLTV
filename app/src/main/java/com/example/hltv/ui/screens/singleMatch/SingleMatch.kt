package com.example.hltv.ui.screens.singleMatch

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.hltv.R
import com.example.hltv.data.convertTimestampToDateClock
import com.example.hltv.data.countdownTimer
import com.example.hltv.data.remote.Media
import com.example.hltv.ui.common.CommonCard

@Composable
fun SingleMatchScreen(matchID: String?, onClickSingleTeam: (String?) -> Unit) {
    val viewModel: SingleMatchViewModel = viewModel()
    LaunchedEffect(Unit) {
        viewModel.loadData(matchID)
        viewModel.loadGames(matchID)
    }
    val event = viewModel.event.value
    val mediaList = viewModel.tournamentMedia.collectAsState()
    val games = viewModel.games
    Column {
        if (viewModel.LiveEvent.value != null) {
            Log.i("SingleMatch", "Drawing liveEvent")
            EventImage(
                teamOneName = event?.homeTeam?.name,
                teamTwoName = event?.awayTeam?.name,
                teamOneLogo = rememberAsyncImagePainter(viewModel.homeTeamIcon.value),
                teamTwoLogo = rememberAsyncImagePainter(viewModel.awayTeamIcon.value),
                teamOneScore = event?.homeScore?.display.toString(),
                teamTwoScore = event?.awayScore?.display.toString(),
                teamOneOnClick = { onClickSingleTeam(event?.homeTeam?.id.toString()) },
                teamTwoOnClick = { onClickSingleTeam(event?.awayTeam?.id.toString()) },
                tournamentIcon = rememberAsyncImagePainter(viewModel.tournamentIcon.value),
                eventStatusType = event?.status?.type,
                changeTimeStamp = event?.changes?.changeTimestamp,
                startTimeStamp = event?.startTimestamp
            )
            PredictionCard(
                teamOneIcon = rememberAsyncImagePainter(viewModel.homeTeamIcon.value),
                teamTwoIcon = rememberAsyncImagePainter(viewModel.awayTeamIcon.value),
                viewModel = viewModel,
                matchID = matchID
            )
        } else if (viewModel.UpcomingEvent.value != null) {
            Log.i("SingleMatch", "Drawing upcomingEvent")
            EventImage(
                teamOneName = event?.homeTeam?.name.toString(),
                teamTwoName = event?.awayTeam?.name.toString(),
                teamOneLogo = rememberAsyncImagePainter(viewModel.homeTeamIcon.value),
                teamTwoLogo = rememberAsyncImagePainter(viewModel.awayTeamIcon.value),
                teamOneScore = event?.homeScore?.display.toString(),
                teamTwoScore = event?.awayScore?.display.toString(),
                teamOneOnClick = { onClickSingleTeam(event?.homeTeam?.id.toString()) },
                teamTwoOnClick = { onClickSingleTeam(event?.awayTeam?.id.toString()) },
                tournamentIcon = rememberAsyncImagePainter(viewModel.tournamentIcon.value),
                eventStatusType = event?.status?.type,
                changeTimeStamp = event?.changes?.changeTimestamp,
                startTimeStamp = event?.startTimestamp
            )
            CommonCard(modifier = Modifier, bottomBox = {
                Text(text = viewModel.description)
            })
            PredictionCard(
                teamOneIcon = rememberAsyncImagePainter(viewModel.homeTeamIcon.value),
                teamTwoIcon = rememberAsyncImagePainter(viewModel.awayTeamIcon.value),
                viewModel = viewModel,
                matchID = matchID,
            )
        } else if (viewModel.FinishedEvent.value != null){
            Log.i("SingleMatch", "Drawing finishedEvent")
            EventImage(
                teamOneName = event?.homeTeam?.name.toString(),
                teamTwoName = event?.awayTeam?.name.toString(),
                teamOneLogo = rememberAsyncImagePainter(viewModel.homeTeamIcon.value),
                teamTwoLogo = rememberAsyncImagePainter(viewModel.awayTeamIcon.value),
                teamOneScore = event?.homeScore?.display.toString(),
                teamTwoScore = event?.awayScore?.display.toString(),
                teamOneOnClick = { onClickSingleTeam(event?.homeTeam?.id.toString()) },
                teamTwoOnClick = { onClickSingleTeam(event?.awayTeam?.id.toString()) },
                tournamentIcon = rememberAsyncImagePainter(viewModel.tournamentIcon.value),
                eventStatusType = event?.status?.type,
                changeTimeStamp = event?.changes?.changeTimestamp,
                startTimeStamp = event?.startTimestamp
            )
            LazyColumn() {
                itemsIndexed(games) { gameNumber, game ->
                    Log.i("games","${games.size}")
                    EventImage(backgroundImage = if (gameNumber < viewModel.mapImages.size) {
                        Log.i("mapImageGameNumber", "$gameNumber")
                        Log.i("mapImageGameNumberImage", "${viewModel.mapImages[gameNumber]}")
                        rememberAsyncImagePainter(viewModel.mapImages[gameNumber])
                    } else rememberAsyncImagePainter(R.drawable.event_background),
                        teamOneScore = game.homeScore?.display.toString(),
                        teamTwoScore = game.awayScore?.display.toString(),
                        teamOneOnClick = { onClickSingleTeam(event?.homeTeam?.id.toString()) },
                        teamTwoOnClick = { onClickSingleTeam(event?.awayTeam?.id.toString()) })
                }
            }
        }
        if (mediaList.value.isNotEmpty()) ShowLiveStreams(mediaList.value)

    }
}


@Composable
fun EventImage(
    backgroundImage: Painter = painterResource(id = R.drawable.event_background),
    tournamentIcon: Painter? = null,
    teamOneName: String? = null,
    teamTwoName: String? = null,
    teamOneLogo: Painter? = null,
    teamTwoLogo: Painter? = null,
    teamOneScore: String,
    teamTwoScore: String,
    teamOneOnClick: () -> Unit,
    teamTwoOnClick: () -> Unit,
    eventStatusType: String? = null,
    changeTimeStamp: Int? = null,
    startTimeStamp: Int? = null,
) {
    val remainingTime by rememberUpdatedState(countdownTimer(startTimeStamp))
    Box(modifier = Modifier.height(180.dp)) {
        Image(
            painter = backgroundImage,
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxWidth()
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
        ) {
            if (teamOneName != null && teamTwoName != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(verticalArrangement = Arrangement.Center) {
                        Text(
                            text = teamOneName.substring(0, minOf(10, teamOneName.length)),
                            color = Color.White,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Right,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                        if (teamOneLogo != null) {
                            Image(painter = teamOneLogo,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(110.dp)
                                    .clickable { teamOneOnClick() })
                        }
                    }
                    Column(verticalArrangement = Arrangement.Center) {
                        tournamentIcon?.let {
                            Image(
                                painter = it,
                                contentDescription = null,
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                            )
                            if(eventStatusType == "inprogress" || eventStatusType == "finished") {
                                Text(
                                    text = "$teamOneScore - $teamTwoScore",
                                    color = Color.White,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Normal,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.align(Alignment.CenterHorizontally)
                                )
                            }
                            else{
                                Text(
                                    text = "Starting",
                                    color = Color.White,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Normal,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.align(Alignment.CenterHorizontally)
                                )
                            }
                            Spacer(modifier = Modifier.height(15.dp))
                            if (eventStatusType == "inprogress") {
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
                            }
                            else if (eventStatusType == "finished") {
                                Text(
                                    text = "Finished",
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Normal,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.align(Alignment.CenterHorizontally)
                                )
                                Text(
                                    text = convertTimestampToDateClock(changeTimeStamp),
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Normal,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.align(Alignment.CenterHorizontally)
                                )
                            }
                            else {
                                Text(
                                    text = remainingTime.toString(),
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Normal,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.align(Alignment.CenterHorizontally)
                                )
                            }
                        }
                    }
                    Column(verticalArrangement = Arrangement.Center) {
                        Text(
                            text = teamTwoName.substring(0, minOf(10, teamTwoName.length)),
                            color = Color.White,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                        if (teamTwoLogo != null) {
                            Image(painter = teamTwoLogo,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(110.dp)
                                    .clickable { teamTwoOnClick() })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ShowLiveStreams(mediaList: ArrayList<Media>) {
    CommonCard(modifier = Modifier.fillMaxWidth(),
        headText = "Livestreams",
        subText = "May or may not have the game",
        bottomBox = {
            Column {
                mediaList.distinctBy { it.url }.forEach { media ->
                    if (media.url != null) {
                        val annotatedString = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                                    textDecoration = TextDecoration.Underline,
                                    fontSize = MaterialTheme.typography.bodyMedium.fontSize
                                )
                            ) {
                                append(media.url.toString())
                                addStringAnnotation(
                                    "URL", media.url.toString(), 0, media.url!!.length
                                )
                            }
                        }

                        val uriHandler = LocalUriHandler.current

                        ClickableText(modifier = Modifier.padding(vertical = 4.dp),
                            text = annotatedString,
                            onClick = { offset ->
                                annotatedString.getStringAnnotations("URL", offset, offset)
                                    .firstOrNull()?.let { annotation ->
                                        uriHandler.openUri(annotation.item)
                                    }
                            })
                    }
                }
            }
        })
}


@Preview(showBackground = true)
@Composable
fun SingleMatchScreenPreview() {
    //SingleMatchScreen(matchID = "1")
}