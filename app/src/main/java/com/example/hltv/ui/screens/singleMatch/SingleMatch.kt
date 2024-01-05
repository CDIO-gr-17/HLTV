package com.example.hltv.ui.screens.singleMatch

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.hltv.R
import com.example.hltv.ui.common.CommonCard

@Composable
fun SingleMatchScreen(matchID : String?){
    val viewModel = SingleMatchViewModel(matchID)
    Column {
        EventImage(
            teamOneLogo = painterResource(id = R.drawable.astralis_logo), teamTwoLogo = painterResource(
                id = R.drawable.astralis_logo
            ), teamOneScore = "35", teamTwoScore = "35"
        )

        PredictionCard(teamOneIcon = painterResource(id = R.drawable.astralis_logo), teamTwoIcon = painterResource(
            id = R.drawable.astralis_logo
        ) , viewModel = viewModel)

        TeamsInEvent(teamOne = painterResource(id = R.drawable.astralis_logo), teamTwo = painterResource(id = R.drawable.astralis_logo), teamThree = painterResource(id = R.drawable.astralis_logo), teamFour = painterResource(id = R.drawable.astralis_logo), teamFive = painterResource(id = R.drawable.astralis_logo), teamSix = painterResource(id = R.drawable.astralis_logo))
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
            )
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun TeamsInEvent(
teamOne: Painter,
teamTwo: Painter,
teamThree: Painter,
teamFour: Painter,
teamFive: Painter,
teamSix: Painter
){
    CommonCard (
        modifier = Modifier, topBox = {
        Box(
                modifier = Modifier
                        .background(color = MaterialTheme.colorScheme.primaryContainer)
                        .fillMaxWidth()
                        .padding(8.dp)
        ){
            Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                        text = "Challengers",
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
        }
    }, bottomBox = {
        Box{

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween)
            {
                Spacer(modifier = Modifier.weight(0.5f))
                Column{
                    Image(
                            painter = teamOne,
                            contentDescription = null,
                            modifier = Modifier
                                    .size(70.dp)
                                    .align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.height(15.dp))

                    Image(
                            painter = teamTwo,
                            contentDescription = null,
                            modifier = Modifier
                                    .size(70.dp)
                                    .align(Alignment.CenterHorizontally)
                    )
                }
                Spacer(modifier = Modifier.weight(0.9f))
                Column{
                    Image(
                            painter = teamThree,
                            contentDescription = null,
                            modifier = Modifier
                                    .size(70.dp)
                                    .align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.height(15.dp))

                    Image(
                            painter = teamFour,
                            contentDescription = null,
                            modifier = Modifier
                                    .size(70.dp)
                                    .align(Alignment.CenterHorizontally)
                    )
                }
                Spacer(modifier = Modifier.weight(0.9f))
                Column{
                    Image(
                            painter = teamFive,
                            contentDescription = null,
                            modifier = Modifier
                                    .size(70.dp)
                                    .align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.height(15.dp))

                    Image(
                            painter = teamSix,
                            contentDescription = null,
                            modifier = Modifier
                                    .size(70.dp)
                                    .align(Alignment.CenterHorizontally)
                    )
                }
                Spacer(modifier = Modifier.weight(0.5f))
            }
        }
    }
    )
}

@Preview(showBackground = true)
@Composable
fun SingleMatchScreenPreview() {
    SingleMatchScreen(matchID = "1")

}