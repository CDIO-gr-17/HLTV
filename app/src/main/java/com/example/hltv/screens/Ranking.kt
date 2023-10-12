package com.example.hltv.screens
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hltv.Greeting
import com.example.hltv.ui.theme.HLTVTheme


val items = (1..20).map { index ->
    ListItem(index, "Item $index Text 1", "Item $index Text 2")
}

data class ListItem(val ranking: Int, val text1: String, val text2: String)

@Composable
fun ItemList(items: List<ListItem>) {
    LazyColumn {
        items(items.size) { index ->
            val item = items[index]
            ItemRow(item = item)
        }
    }
}

@Composable
fun ItemRow(item: ListItem) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${item.ranking}.",
            modifier = Modifier.padding(16.dp),
            //style = MaterialTheme.typography.h6
        )
        Column {
            Text(text = item.text1, style = MaterialTheme.typography.bodyLarge)
            Text(text = item.text2, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RankingPreview() {
    ItemList(items = items)
}