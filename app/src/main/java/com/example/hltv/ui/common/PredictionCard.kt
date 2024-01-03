package com.example.hltv.ui.common


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

var hasVoted : MutableState<Boolean> = mutableStateOf(false)
val borderColor : MutableState<Color> = mutableStateOf(Color.Black)
val borderWidth : MutableState<Dp> = mutableStateOf(2.dp)

@Composable
fun PredictionCard(
    modifier: Modifier = Modifier,
    teamOneIcon: Painter,
    teamTwoIcon: Painter,
    teamOneColor: Color = Color.Blue,
    teamTwoColor: Color = Color.Red,
) {
    CommonCard(modifier = modifier, customInnerPadding = 0.dp, topBox = {
        Box(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.primaryContainer)
                .fillMaxWidth()
                .padding(8.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Get your prediction in!",
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                    Text(
                        text = "Vote for the team you think will win",
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }

            }
        }
    }, bottomBox = {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        listOf(
                            teamOneColor, teamTwoColor
                        ),

                        )
                )
        ) {
            Row(
                modifier = modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .align(Alignment.Center),
                horizontalArrangement = Arrangement.SpaceAround

            ) {

                drawCircle(
                    modifier = modifier,
                    teamIcon = teamOneIcon,
                    onClick = {
                        hasVoted = mutableStateOf(true)
                    }
                )
                Text(
                    text = "VS",
                    modifier = modifier
                        .padding(8.dp)
                        .align(Alignment.CenterVertically),
                    fontSize = 65.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    textAlign = TextAlign.Center,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                    )
                drawCircle(
                    modifier = modifier,
                    teamIcon = teamTwoIcon,
                    onClick = {
                        hasVoted = mutableStateOf(true)
                    }
                )
            }
        }
    })
}

@Composable
private fun drawCircle(modifier: Modifier, teamIcon: Painter, onClick: () -> Unit) {
    Box(modifier =
    if (hasVoted.value) {
        modifier
            .size(100.dp)
            .border(4.dp, Color.White, CircleShape)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.onPrimaryContainer)
            .clickable {
                onClick()
            }
    } else {
        modifier
            .size(100.dp)
            .border(2.dp, Color.Black, CircleShape)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.onPrimaryContainer)
            .clickable {
                onClick()
            }
    }

    ) {
        Image(
            painter = teamIcon,
            contentDescription = "teamIcon",
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)
                .padding(16.dp)
        )
        if (hasVoted.value) drawText()

    }

}
fun drawText() {

}

@Preview
@Composable
fun PredictionCardPreview() {
    PredictionCard(
        teamOneIcon = painterResource(id = com.example.hltv.R.drawable.astralis_logo),
        teamTwoIcon = painterResource(id = com.example.hltv.R.drawable.astralis_logo),

    )
}