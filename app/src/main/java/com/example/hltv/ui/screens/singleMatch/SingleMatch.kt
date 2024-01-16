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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.hltv.R
import com.example.hltv.data.CountdownViewModel
import com.example.hltv.data.convertTimestampToDateClock
import com.example.hltv.data.formatTime
import com.example.hltv.data.remote.Media
import com.example.hltv.ui.common.CommonCard


@Composable
fun SingleMatchScreen(matchID: String?, onClickSingleTeam: (String?) -> Unit) {
    val viewModel: SingleMatchViewModel = viewModel()
    val countdownViewModel = viewModel(CountdownViewModel::class.java)
    LaunchedEffect(Unit) {
        viewModel.loadData(matchID)
        viewModel.loadGames(matchID)
    }
    val event = viewModel.event.value
    val mediaList = viewModel.tournamentMedia.collectAsState()
    val games = viewModel.games
    LazyColumn {
        if (viewModel.LiveEvent.value != null) {
            item {
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
            }
            item{
                PredictionCard(
                    teamOneColor = viewModel.homeTeamColor.value,
                    teamTwoColor = viewModel.awayTeamColor.value,
                    teamOneIcon = rememberAsyncImagePainter(viewModel.homeTeamIcon.value),
                    teamTwoIcon = rememberAsyncImagePainter(viewModel.awayTeamIcon.value),
                    viewModel = viewModel,
                    matchID = matchID,
                    finished = true
                )
            }
        } else if (viewModel.UpcomingEvent.value != null) {
            item {
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
                    startTimeStamp = event?.startTimestamp,
                    countdownViewModel = countdownViewModel
                )
            }
            item {
                CommonCard(modifier = Modifier) {
                    Text(text = viewModel.description)
                }
            }
            item {
                PredictionCard(
                    teamOneColor = viewModel.homeTeamColor.value,
                    teamTwoColor = viewModel.awayTeamColor.value,
                    teamOneIcon = rememberAsyncImagePainter(viewModel.homeTeamIcon.value),
                    teamTwoIcon = rememberAsyncImagePainter(viewModel.awayTeamIcon.value),
                    viewModel = viewModel,
                    matchID = matchID,
                )
            }
        } else if (viewModel.FinishedEvent.value != null) {
            item {
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
            }
            item {
                CommonCard(
                    modifier = Modifier,
                    headText = "Map results"
                ) {
                    Column {
                        games.forEachIndexed { gameNumber, game ->
                            Log.i("games", "${games.size}")
                            EventImage(
                                backgroundImage = if (gameNumber < viewModel.mapImages.size) {
                                    Log.i("mapImageGameNumber", "$gameNumber")
                                    Log.i(
                                        "mapImageGameNumberImage",
                                        "${viewModel.mapImages[gameNumber]}"
                                    )
                                    rememberAsyncImagePainter(viewModel.mapImages[gameNumber])
                                } else rememberAsyncImagePainter(R.drawable.event_background),
                                teamOneScore = game.homeScore?.display.toString(),
                                teamTwoScore = game.awayScore?.display.toString(),
                                teamOneOnClick = { onClickSingleTeam(event?.homeTeam?.id.toString()) },
                                teamTwoOnClick = { onClickSingleTeam(event?.awayTeam?.id.toString()) },
                                crop = true,
                                mapName = game.map?.name
                            )
                            if (gameNumber < games.size - 1) {
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }
            item{
                PredictionCard(
                    teamOneColor = viewModel.homeTeamColor.value,
                    teamTwoColor = viewModel.awayTeamColor.value,
                    teamOneIcon = rememberAsyncImagePainter(viewModel.homeTeamIcon.value),
                    teamTwoIcon = rememberAsyncImagePainter(viewModel.awayTeamIcon.value),
                    viewModel = viewModel,
                    matchID = matchID,
                    finished = true
                )
            }
        }
        item {
            if (mediaList.value.isNotEmpty())
                ShowLiveStreams(mediaList.value)
        }
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
    crop: Boolean ?= false,
    mapName: String?= null,
    countdownViewModel: CountdownViewModel = viewModel()
) {
    LaunchedEffect(key1 = countdownViewModel) {
        if (startTimeStamp != null) {
            countdownViewModel.startCountdown(startTimeStamp)
        }
    }
    val remainingTime by countdownViewModel.remainingTime.collectAsState()
    DisposableEffect(Unit) {
        if (startTimeStamp != null) {
            countdownViewModel.startCountdown(startTimeStamp)
        }
        onDispose {
            countdownViewModel.stopCountdown()
        }
    }
    val formattedTime = remainingTime?.formatTime() ?: "00:00:00"


    Box(modifier = Modifier) {
        if (crop == true) {
            Image(
                painter = backgroundImage,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .align(Alignment.TopCenter)
            )
            Column(verticalArrangement = Arrangement.Center) {
                if (mapName != null) {
                    Text(
                        text = mapName,
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Right,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .clip(
                                RoundedCornerShape(
                                    topStart = 0.dp,
                                    topEnd = 0.dp,
                                    bottomEnd = 10.dp,
                                    bottomStart = 10.dp
                                )
                            )
                            .background(MaterialTheme.colorScheme.secondaryContainer)
                            .padding(top = 0.dp, bottom = 3.dp, start = 4.dp, end = 4.dp)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    if(teamOneScore != "null" && teamTwoScore != "null") {
                        Text(
                            text = teamOneScore,
                            color = Color.White,
                            fontSize = 40.sp,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier
                                .align(alignment = Alignment.CenterVertically)
                                .shadow(
                                    10.dp,
                                    shape = CircleShape
                                )
                        )
                        Text(
                            text = teamTwoScore,
                            color = Color.White,
                            fontSize = 40.sp,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier
                                .align(alignment = Alignment.CenterVertically)
                                .shadow(20.dp, shape = CircleShape)
                        )
                    }
                    else{
                        Text(
                            text = "N/A",
                            letterSpacing = 0.6.em,
                            color = Color.White,
                            fontSize = 40.sp,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier
                                .align(alignment = Alignment.CenterVertically)
                                .shadow(20.dp, shape = CircleShape)
                        )
                    }

                }

            }
        } else {
            Image(
                painter = backgroundImage,
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )
            if (teamOneName != null && teamTwoName != null) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .height(180.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = teamOneName.substring(0, minOf(10, teamOneName.length)),
                            color = Color.White,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Normal,
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
                    Column{
                        if (tournamentIcon != null) {
                            Image(
                                painter = tournamentIcon,
                                contentDescription = null,
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .shadow(20.dp, shape = CircleShape)
                            )
                        }
                        if (eventStatusType == "inprogress" || eventStatusType == "finished") {
                            Text(
                                text = "$teamOneScore - $teamTwoScore",
                                color = Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Normal,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                        }
                        Spacer(modifier = Modifier.height(15.dp))
                        when (eventStatusType) {
                            "inprogress" -> {
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
                            "finished" -> {
                                Text(
                                    text = "Ended",
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
                            else -> {
                                Text(
                                    text = formattedTime,
                                    color = Color.White,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Normal,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.align(Alignment.CenterHorizontally)
                                )
                            }
                        }
                    }
                    Column {
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
    CommonCard(
        modifier = Modifier.fillMaxWidth(),
        headText = "Livestreams",
        subText = "May or may not have the game"
    ) {
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
    }
}


@Preview(showBackground = true)
@Composable
fun SingleMatchScreenPreview() {
    //SingleMatchScreen(matchID = "1")
}