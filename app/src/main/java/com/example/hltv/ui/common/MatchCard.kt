package com.example.hltv.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hltv.R
import com.example.hltv.ui.screens.homeScreen.M

@Composable
fun MatchCard(){
    UpcomingMatchCard(
        teamOneName = "Astralis1",
        teamOneIcon = painterResource(id = R.drawable.astralis_logo),
        teamTwoName = "Astralis2",
        teamTwoIcon = painterResource(id = R.drawable.astralis_logo),
        matchDate = "Tuesday 22/10 - 18:00"
    )
}

@Composable
fun LiveMatchCard(
    modifier: Modifier = Modifier,
    teamOneName: String,
    teamOneIcon: Painter,
    teamOneScore: Int,
    teamOneOnClick: () -> Unit,
    teamTwoName: String,
    teamTwoIcon: Painter,
    teamTwoScore: Int,
    teamTwoOnClick: () -> Unit,

    ) {
    CommonCard(
        modifier = modifier.testTag("LiveMatchCard"),
        headText = "Your match is live!",
        image = painterResource(id = R.drawable.pngtree_icon_live_streaming_vector_png_image_4643886),
        bottomBox = {
            Row {
                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = modifier
                        .fillMaxWidth(0.5f)
                        .clickable { teamOneOnClick() }) {
                    Text(
                        text = teamOneName,
                        modifier = modifier
                            .padding(8.dp),
                        fontSize = M.typography.bodyLarge.fontSize,
                        color = M.colorScheme.onSecondaryContainer,
                        textAlign = TextAlign.Center,
                    )
                    Image(
                        painter = teamOneIcon,
                        contentDescription = "teamOneIcon",
                        modifier = Modifier
                            .padding(4.dp)
                            .size(50.dp, 50.dp),


                        )
                    Text(
                        text = teamOneScore.toString(),
                        fontSize = M.typography.displayLarge.fontSize
                    )
                }

                Divider(
                    modifier = Modifier
                        .width(1.dp)
                        .padding(vertical = 4.dp),
                    color = M.colorScheme.onBackground
                )

                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { teamTwoOnClick() }) {
                    Text(
                        text = teamTwoName,
                        modifier = Modifier
                            .padding(8.dp),
                        fontSize = M.typography.bodyLarge.fontSize,
                        color = M.colorScheme.onSecondaryContainer,
                        textAlign = TextAlign.Center,
                    )
                    Image(
                        painter = teamTwoIcon,
                        contentDescription = "teamOneIcon",
                        modifier = Modifier
                            .padding(4.dp)
                            .size(50.dp, 50.dp),
                    )
                    Text(
                        text = teamTwoScore.toString(),
                        fontSize = M.typography.displayLarge.fontSize
                    )
                }
            }

        }

    )

}

@Composable
fun UpcomingMatchCard(
    modifier: Modifier = Modifier,
    teamOneName: String,
    teamOneIcon: Painter,
    teamOneOnClick: (() -> Unit?)? = null,
    teamTwoName: String,
    teamTwoIcon: Painter,
    teamTwoOnClick: (() -> Unit?)? = null,
    matchDate: String

    ) {
    CommonCard(
        modifier = modifier.testTag("UpcomingMatchCard"),
        headText = matchDate,
        bottomBox = {
            Row {
                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = modifier
                        .fillMaxWidth(0.5f)
                        .clickable {
                            if (teamOneOnClick != null) {
                                teamOneOnClick()
                            }
                        }) {
                    Text(
                        text = teamOneName,
                        modifier = modifier
                            .padding(8.dp),
                        fontSize = M.typography.bodyLarge.fontSize,
                        color = M.colorScheme.onSecondaryContainer,
                        textAlign = TextAlign.Center,
                    )
                    Image(
                        painter = teamOneIcon,
                        contentDescription = "teamOneIcon",
                        modifier = Modifier
                            .padding(4.dp)
                            .size(50.dp, 50.dp),


                        )
                }

                Divider(
                    modifier = Modifier
                        .width(1.dp)
                        .padding(vertical = 4.dp),
                    color = M.colorScheme.onBackground
                )

                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (teamTwoOnClick != null) {
                                teamTwoOnClick()
                            }
                        }) {
                    Text(
                        text = teamTwoName,
                        modifier = Modifier
                            .padding(8.dp),
                        fontSize = M.typography.bodyLarge.fontSize,
                        color = M.colorScheme.onSecondaryContainer,
                        textAlign = TextAlign.Center,
                    )
                    Image(
                        painter = teamTwoIcon,
                        contentDescription = "teamOneIcon",
                        modifier = Modifier
                            .padding(4.dp)
                            .size(50.dp, 50.dp),
                    )
                }
            }

        }

    )

}

@Preview
@Composable
fun MatchCardPreview(){
    MatchCard()
}