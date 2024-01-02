package com.example.hltv.ui.common


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun PredictionCard(
    modifier: Modifier = Modifier,
    teamOneIcon: Painter,
    teamTwoIcon: Painter,
    teamOneColor: Color,
    teamTwoColor: Color,
) {
    CommonCard(
        modifier = modifier,
        customInnerPadding = 0.dp,
        topBox = {
            Box(
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.primaryContainer)
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
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
        },
        bottomBox = {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.horizontalGradient(
                            listOf(
                                teamOneColor,
                                teamTwoColor
                            )
                        )
                    )
            ) {
                Row(modifier = modifier
                    .padding(8.dp)
                    .fillMaxWidth()

                ) {
                    Box(
                        modifier = modifier
                            .size(100.dp)
                            .border(2.dp, Color.Black, CircleShape)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.onPrimaryContainer)

                    )
                    Box(
                        modifier = modifier
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.onPrimaryContainer)
                            .size(100.dp)
                            .padding(8.dp)
                    )

                }


            }


        }
    )

}

@Composable
fun drawCircle() {
    Box(
        modifier = Modifier
            .size(100.dp)
            .border(2.dp, Color.Black, CircleShape)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.onPrimaryContainer)

    )

}

@Preview
@Composable
fun PredictionCardPreview() {
    PredictionCard(
        teamOneIcon = painterResource(id = com.example.hltv.R.drawable.astralis_logo),
        teamTwoIcon = painterResource(id = com.example.hltv.R.drawable.astralis_logo),
        teamOneColor = Color.Red,
        teamTwoColor = Color.Blue
    )
}