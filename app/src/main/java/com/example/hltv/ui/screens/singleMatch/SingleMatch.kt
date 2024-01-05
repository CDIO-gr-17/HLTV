package com.example.hltv.ui.screens.singleMatch

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hltv.R


@Composable
fun SingleMatchScreen(matchID: String?) {
    val viewModel = SingleMatchViewModel(matchID)
    viewModel.loadGames()
    val games = viewModel.games

    Column {
        viewModel.games.forEachIndexed { index ,game ->

            GameInfo(
                backgroundImage = viewModel.mapImages[index]?.asImageBitmap(),
                trophyImage = null,
                teamOneLogo = viewModel.teamImages[0]?.asImageBitmap(),
                teamTwoLogo = viewModel.teamImages[1]?.asImageBitmap(),
                teamOneScore = if (game.homeScore?.display == null) "N/A" else game.homeScore?.display.toString(),
                teamTwoScore = if (game.awayScore?.display == null) "N/A" else game.awayScore?.display.toString(),
            )
        }
        PredictionCard(
            teamOneIcon = viewModel.teamImages[0]?.asImageBitmap(),
            teamTwoIcon = viewModel.teamImages[1]?.asImageBitmap(),
            viewModel = viewModel
        )
    }

}

//BitmapFactory.decodeResource(LocalContext.current.resources, R.drawable.astralis_logo)

@Composable
fun GameInfo(
    backgroundImage: ImageBitmap?,
    trophyImage: ImageBitmap? ,
    teamOneLogo: ImageBitmap?,
    teamTwoLogo: ImageBitmap?,
    teamOneScore: String,
    teamTwoScore: String,
    isLive: Boolean = false
) {
    Box(modifier = Modifier.height(180.dp)) {
        Image(
            bitmap = if (backgroundImage == null) BitmapFactory.decodeResource(
                LocalContext.current.resources, R.drawable.event_background
            ).asImageBitmap()
            else backgroundImage,
            contentDescription = "background image",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Image(
                bitmap = if (teamOneLogo == null) BitmapFactory.decodeResource(
                    LocalContext.current.resources, R.drawable.astralis_logo
                ).asImageBitmap()
                else teamOneLogo,
                contentDescription = "team one logo",
                modifier = Modifier
                    .size(110.dp)
                    .align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.weight(1f))
            Column {
                Spacer(modifier = Modifier.weight(1f))

                Image(
                    bitmap = if (trophyImage == null) BitmapFactory.decodeResource(
                        LocalContext.current.resources, R.drawable.astralis_logo
                    ).asImageBitmap()
                    else trophyImage,
                    contentDescription = "trophy image",
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
                if (isLive) {
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
                Spacer(modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.weight(1f))
            Image(
                bitmap = if (teamTwoLogo == null) BitmapFactory.decodeResource(
                    LocalContext.current.resources, R.drawable.astralis_logo
                ).asImageBitmap()
                else teamTwoLogo,
                contentDescription = "team two logo",
                modifier = Modifier
                    .size(110.dp)
                    .align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SingleMatchScreenPreview() {
    SingleMatchScreen(matchID = "1")

}