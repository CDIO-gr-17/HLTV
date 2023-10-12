@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.hltv.screens

import android.graphics.Paint.Align
import android.graphics.drawable.Drawable
import android.graphics.drawable.Icon
import android.preference.PreferenceActivity.Header
import androidx.compose.animation.VectorConverter
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment


@Composable
fun HomeScreen () {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Card (
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .height(200.dp)


        ){
            Box(modifier = Modifier
                .background(color = Color.Magenta)
                .fillMaxWidth()
                .height(50.dp)
            ) {
                Row {
                    Text(
                        text = "Your match is live!",
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxHeight()
                            .weight(1f),
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.primary,
                        )

                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Live Icon",
                        modifier = Modifier
                            .padding(12.dp)
                        )

                }
            }
            Row {
                Column {
                    Text(text = "Astralis",
                        modifier = Modifier
                            .padding(8.dp)
                            .align(alignment = CenterHorizontally),
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center,



                        )
                    Icon(imageVector = Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(12.dp)
                        )
                }



            }



        }
        Divider(modifier = Modifier.padding(horizontal = 8.dp))
        Card (
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .height(150.dp)


        ){
            Text(text = "Your match is live!",
                modifier = Modifier
                    .padding(8.dp),
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.primary,


                )
        }
        Card (
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .height(150.dp)


        ){
            Text(text = "Your match is live!",
                modifier = Modifier
                    .padding(8.dp),
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.primary,


                )
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}