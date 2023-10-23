@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.hltv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.hltv.screens.HomeScreen
import com.example.hltv.ui.theme.HLTVTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

                    HLTVApp()
                }



    }
}

@Composable
fun HLTVApp() {
    HLTVTheme {
        val navController = rememberNavController()
        Scaffold (
            topBar = {
                TopAppBar(title = { Text(text = "Test")})

            }
        ) {
            mainNavHost(
                navController = navController,
                modifier = Modifier.padding(it))
        }

    }
}
