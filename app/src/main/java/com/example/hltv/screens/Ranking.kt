package com.example.hltv.screens
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    LazyColumn {
        items(items.size) {index ->
            CardRow(team = "Astralis", subtext = "RUSH B", index+1)
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

@Preview(showBackground = true)
@Composable
fun RankingPreview() {
    ItemList(items = items)
}