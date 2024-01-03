package com.example.hltv.ui.screens.singleMatch


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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hltv.ui.common.CommonCard

var hasVoted : MutableState<Int>? = null
var isHomeTeamSelected : MutableState<Boolean> = mutableStateOf(false)
val borderColor : MutableState<Color> = mutableStateOf(Color.Black)
val borderWidth : MutableState<Dp> = mutableStateOf(2.dp)

@Composable
fun PredictionCard(
    modifier: Modifier = Modifier,
    teamOneIcon: Painter,
    teamTwoIcon: Painter,
    teamOneColor: Color = Color.Blue,
    teamTwoColor: Color = Color.Red,
    viewModel : SingleMatchViewModel
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
                    voteCount = viewModel.prediction.value.homeTeamVotePercentage,
                    hometeam = true,
                    onClick = {
                        hasVoted = mutableStateOf(1)
                        isHomeTeamSelected.value = true
                        viewModel.updatePrediction(1)
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
                    fontWeight = FontWeight.Bold,
                    )
                drawCircle(
                    modifier = modifier,
                    teamIcon = teamTwoIcon,
                    voteCount = viewModel.prediction.value.awayTeamVotePercentage,
                    hometeam = false,
                    onClick = {
                        hasVoted = mutableStateOf(2)
                        isHomeTeamSelected.value = false
                        viewModel.updatePrediction(2)
                    }
                )
            }
        }
    })
}

@Composable
private fun drawCircle(modifier: Modifier, teamIcon: Painter, voteCount : Int, hometeam : Boolean,onClick: () -> Unit) {
    Column {
        Box(modifier =
        if (hasVoted !=null) {
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
        }
        if (hasVoted != null){

                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "mark",
                    tint = Color.Black,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .offset(y = (-30).dp, x = (25).dp)
                        .size(35.dp)
                        .alpha(if ((isHomeTeamSelected.value && hometeam) || (!hometeam && !isHomeTeamSelected.value)) {
                            1f
                        } else {
                            0f
                        })
                )

                Text(
                    text = voteCount.toString() + "%",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        //.padding(8.dp)
                        .offset(y = (-25).dp),
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                )
            }
    }}


@Preview
@Composable
fun PredictionCardPreview() {
}