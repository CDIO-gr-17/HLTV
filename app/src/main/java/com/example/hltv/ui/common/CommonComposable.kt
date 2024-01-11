package com.example.hltv.ui.common

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.hltv.data.local.PrefDataKeyValueStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@Composable
//Eksempelkode for commonComposables
fun CommonComposable() {
    LazyColumn{
        item {
            //Eksempel med brug af headText og subText uden topBox
           CommonCard(
               modifier = Modifier.fillMaxWidth(),
               headText = "Blast Premier world final 2023", //Valgfri - Erstatning for topBox
               subText = "Oct. 13 - Nov. 13", //Valgfri - Erstatning for topBox
               image = painterResource(id = com.example.hltv.R.drawable.astralis_logo),
               bottomBox = {
                   Box {
                       Text(
                           text = "Eventinformation",
                           fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                           color = MaterialTheme.colorScheme.onSecondaryContainer,
                           modifier = Modifier
                               .align(Alignment.CenterStart)
                       )
                   }
               }
           )
            //Eksempel med brug af topBox og bottomBox
            CommonCard(
                modifier = Modifier.fillMaxWidth(),
                topBox = { //Valgfri - Erstatning for headText / subText
                    Box {
                        Text(
                            text = "topBox information",
                            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                        )
                    }
                },
                bottomBox = {
                    Box {
                        Text(
                            text = "bottomBox information",
                            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                        )
                    }
                }
            )
            //Eksempel med brug af topBox uden bottomBox
            CommonCard(
                modifier = Modifier.fillMaxWidth(),
                topBox = { //Valgfri - Erstatning for headText / subText
                    Box {
                        Text(
                            text = "topBox information",
                            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                        )
                    }
                }
            )
            CommonCard (
                modifier = Modifier.fillMaxWidth(),
                bottomBox = {
                    Box{
                        Text(
                            text = "bottomBox information",
                            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
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
fun CommonCard(
    modifier: Modifier,
    headText: String ?= null,
    subText: String ?= null,
    image: Painter?= null,
    customOuterPadding: Dp ?= null,
    customInnerPadding: Dp ?= null,
    topBox: @Composable (BoxScope.() -> Unit?)? = null,
    bottomBox: @Composable (BoxScope.() -> Unit?)? = null,
) =
    Card (
        modifier = modifier
            .padding(customOuterPadding ?: 8.dp)
            //.height(IntrinsicSize.Max) Removed for expanded bottom. May break smth?
    ) {
        if (topBox != null || headText != null || subText != null || image != null)
            Box(
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.primaryContainer)
                    .fillMaxWidth()
                    .padding(customInnerPadding ?: 8.dp)
            ) {
                if(topBox != null)
                    topBox()
                else {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            headText?.let {
                                Text(
                                    text = it,
                                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                )
                            }
                            subText?.let {
                                Text(
                                    text = it,
                                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                )
                            }
                        }
                        image?.let {
                            Image(
                                painter = image,
                                contentDescription = null,
                                alignment = Alignment.CenterEnd,
                                modifier = Modifier
                                    .size(40.dp)

                            )
                        }
                    }
                }
        }
        bottomBox?.let { bottomBox ->
            Box(
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.secondaryContainer)
                    .fillMaxWidth()
                    .padding(customInnerPadding ?: 8.dp)
            ) {
                bottomBox()
            }
        }
    }
@Composable
fun FavoriteButton(
    datastore: PrefDataKeyValueStore,
    teamID: Int,
    modifier: Modifier = Modifier,
    color: Color = Color(0xffE91E63)
) {

    var isFavorite by remember { mutableStateOf(false) }

    IconToggleButton(
        checked = isFavorite,
        onCheckedChange = {
            isFavorite = !isFavorite
            CoroutineScope(Dispatchers.IO).launch {
                datastore.updateFavouriteTeam(teamID)
                val temp = datastore.getFavouriteTeam().collect()
                Log.d("FavoriteButton", "Favourite team is now: $temp vs  $teamID")
            }


        }
    ) {
        Icon(
            tint = color,
            modifier = modifier.graphicsLayer {
                scaleX = 1.3f
                scaleY = 1.3f
            },
            imageVector = if (isFavorite) {
                Icons.Filled.Favorite
            } else {
                Icons.Default.FavoriteBorder
            },
            contentDescription = null
        )
    }

}






/*@Composable
fun FavoriteButton(isFavorite: Boolean) {
    Icon(
        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Outlined.Favorite,
      contentDescription = if (isFavorite) ("Favorited") else ("Not favorited"),
        tint = if (isFavorite) Color.Red else Color.Gray
    )
}

*/
@Preview
@Composable
fun CommonComposablePreview() {
    CommonComposable()
}