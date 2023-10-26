package com.example.hltv.screens
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.example.hltv.R

val items = (1..20).map { index ->
    ListItem(index, "Item $index Text 1", "Item $index Text 2")
}
data class ListItem(val ranking: Int, val text1: String, val text2: String)

@Composable
fun ItemList(items: List<ListItem>) {
    val R = MaterialTheme
    LazyColumn {
        items(items.size) {index ->
            //CardRow(team = "Astralis", subtext = "RUSH B", index+1)
            teamCard(modifier = Modifier, R = R, text1 = (index + 1).toString() + "  Holdnavn", text2 = "Scoop")
            if (index < items.size-1){
                Spacer(modifier = Modifier.height(1.dp))
            }
        }
    }
}

@Composable
fun CardRow(team: String, subtext: String, index: Int) {Card(
    colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
    ),
    modifier = Modifier
        .fillMaxWidth()
        .height(80.dp)
        ){
            Row(modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically) {
                Text("#$index ",
                    fontSize = 36.sp
                    )
                //Image(bitmap = artist.image, contentDescription = "Artist image")
                Column (){
                    Text(team, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 4.dp), fontSize = 16.sp)
                    Text(subtext, fontSize = 14.sp)
                }
                Image(
                    painter = painterResource(id = R.drawable.astralis_logo),
                    contentDescription = null, //TODO
                    alignment = Alignment.TopEnd,
                    modifier = Modifier.fillMaxSize().padding(2.dp)//.padding(start = 120.dp)
                )
            }
        }
}

@Composable
fun teamCard(modifier: Modifier, R: MaterialTheme, text1: String, text2: String) =
    Card (
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(200.dp)


    ){
        Box(modifier = Modifier
            .background(color = Color.Magenta)
            .fillMaxWidth()
            .height(42.dp)
        ) {
            Text(
                text = text1,
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxHeight(),
                    //.weight(1f),
                fontSize = R.typography.bodyLarge.fontSize,
                color = R.colorScheme.primary,
            )
        }
        Row () {
            for (i in 1..5){
                Column () {
                    Image(
                        painter = painterResource(id = com.example.hltv.R.drawable.astralis_logo),
                        contentDescription = null, //TODO
                        alignment = Alignment.TopStart,
                        modifier = Modifier.size(69.dp)
                    )
                    Text(
                        text = "Name",
                        fontSize = R.typography.bodyLarge.fontSize,
                        color = R.colorScheme.primary,
                    )
                }
            }

        }
    }


@Preview(showBackground = true)
@Composable
fun RankingPreview() {
    ItemList(items = items)
}