package com.example.hltv.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CommonComposable() {
    LazyColumn{
        item {
           TeamCard(
               modifier = Modifier,
               R = MaterialTheme,
               headText = "Blast Premier world final 2023",
               subText = "NOW",
               cardWidth = Modifier.fillMaxWidth(),
               box = {
                   Box(modifier = Modifier
                   ) {
                       Text(
                           text = "Test",
                           fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                           color = MaterialTheme.colorScheme.primary,
                           modifier = Modifier
                               .align(Alignment.CenterStart)
                       )
                   }
               }
           )
       }
    }
}

@Composable
fun TeamCard(
    modifier: Modifier,
    R: MaterialTheme,
    headText: String,
    subText: String ?= null,
    cardWidth: Modifier,
    box: @Composable() BoxScope.() -> Unit
) =
    Card (
        modifier = modifier
            .then(cardWidth)
            .padding(8.dp)
            .height(IntrinsicSize.Max)
    ) {
        Box(
            modifier = Modifier
                .background(color = Color.DarkGray)
                .fillMaxWidth()
                .height(IntrinsicSize.Max)
                .padding(8.dp)
        ) {
            Column {
                Text(
                    text = headText,
                    fontSize = R.typography.bodyLarge.fontSize,
                    color = R.colorScheme.primary,
                    modifier = Modifier
                        .padding(start = 10.dp)
                )
                subText?.let {
                    Text(
                        text = it,
                        fontSize = R.typography.bodyMedium.fontSize,
                        color = R.colorScheme.primary,
                        modifier = Modifier
                            .padding(start = 10.dp)
                    )
                }
            }
        }
        Box(modifier = Modifier
            .padding(8.dp)
            .height(IntrinsicSize.Max)
        ){
            box()
        }
    }


@Preview(showBackground = true)
@Composable
fun CommonComposablePreview() {
    CommonComposable()
}