package com.example.hltv.ui.screens.singleMatch

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hltv.R


@Composable
fun SingleMatchScreen(matchID : String?){
    val viewModel = SingleMatchViewModel(matchID)
    viewModel.loadGames()
    val games = viewModel.games.value.events
    Column {
        val root = Environment.getExternalStorageDirectory().toString()

        games.forEach { game ->

            GameInfo(
                teamOneLogo = viewModel.teamImages[0],
                teamTwoLogo = viewModel.teamImages[1],
                teamOneScore = game.homeScore?.current.toString(),
                teamTwoScore = game.awayScore?.display.toString()
            )
        }
        PredictionCard(teamOneIcon = painterResource(id = R.drawable.astralis_logo), teamTwoIcon = painterResource(
            id = R.drawable.astralis_logo
        ) , viewModel = viewModel)
    }

}

//BitmapFactory.decodeResource(LocalContext.current.resources, R.drawable.astralis_logo)

@Composable
fun GameInfo(
    backgroundImage: Painter = painterResource(id = R.drawable.event_background),
    trophyImage: Painter = painterResource(id = R.drawable.trophy_24px),
    teamOneLogo: Bitmap?,
    teamTwoLogo: Bitmap?,
    teamOneScore: String = "N/A",
    teamTwoScore: String = "N/A",
) {
    Box (modifier = Modifier.height(180.dp)){
        Image(
            painter = backgroundImage,
            contentDescription = "background image",
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
                bitmap = if (teamOneLogo == null) BitmapFactory.decodeResource(
                    LocalContext.current.resources,
                    R.drawable.diversity_3_24px).asImageBitmap()
                else teamOneLogo.asImageBitmap(),
                contentDescription = "team one logo",
                modifier = Modifier
                    .size(110.dp)
                    .align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.weight(1f))
            Column {
                Spacer(modifier = Modifier.weight(1f))

                Image(
                    painter = trophyImage,
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
                bitmap = if (teamTwoLogo == null) BitmapFactory.decodeResource(
                    LocalContext.current.resources,
                    R.drawable.diversity_3_24px).asImageBitmap()
                else teamTwoLogo.asImageBitmap(),
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