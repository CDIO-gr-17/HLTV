package com.example.hltv.ui.screens.singleMatch

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.hltv.data.remote.Media
import com.example.hltv.ui.common.CommonCard

@Composable
fun SingleMatchScreen(matchID: String?, onClickSingleTeam: (String?) -> Unit) {
    val viewModel: SingleMatchViewModel = viewModel()
    LaunchedEffect(Unit) {
        viewModel.loadData(matchID)
    }
    val event = viewModel.event.value
    Column {

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

        val mediaList = viewModel.tournamentMedia.value.media
        ShowLiveStreams(mediaList)

    }
}


@Composable
fun EventImage(
    backgroundImage: Painter = painterResource(id = R.drawable.event_background),
    trophyImage: Painter = painterResource(id = R.drawable.trophy_24px),
    teamOneLogo: Painter,
    teamTwoLogo: Painter,
    teamOneScore: String,
    teamTwoScore: String,
    teamOneOnClick: () -> Unit,
    teamTwoOnClick: () -> Unit,
) {
    Box(modifier = Modifier.height(180.dp)) {
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

                Image(
                    painter = trophyImage,
                    contentDescription = null,
                    modifier = Modifier
                        .size(50.dp)
                        .align(Alignment.CenterHorizontally)
                )
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

@Composable
fun ShowLiveStreams(mediaList: List<Media>) {
    if (mediaList.isEmpty())
        return
    CommonCard(
        modifier = Modifier.fillMaxWidth(),
        headText = "Livestreams",
        subText = "May or may not have the game",
        bottomBox = {
            Column {
                mediaList.distinctBy { it.url }.forEach { media ->
                    if(media.url != null) {
                        val annotatedString = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    color = Color.Blue,
                                    textDecoration = TextDecoration.Underline,
                                    fontSize = MaterialTheme.typography.bodyMedium.fontSize
                                )
                            ) {
                                append(media.url.toString())
                                addStringAnnotation(
                                    "URL",
                                    media.url.toString(),
                                    0,
                                    media.url!!.length
                                )
                            }
                        }

                        val uriHandler = LocalUriHandler.current

                        ClickableText(
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
    )
}


@Preview(showBackground = true)
@Composable
fun SingleMatchScreenPreview() {
    //SingleMatchScreen(matchID = "1")
}