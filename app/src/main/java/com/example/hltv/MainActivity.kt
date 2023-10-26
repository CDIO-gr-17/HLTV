@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.hltv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.Navigation
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
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
        val currentBackStack by navController.currentBackStackEntryAsState()
        val currentDestination = currentBackStack?.destination
        val currentScreen =
            bottomAppBarRowScreens.find { it.route == currentDestination?.route } ?: Home

        Scaffold (
            topBar = {
                TopAppBar(title = { Text(text = currentScreen.route)}
                )
            },
            bottomBar = {
                BottomAppBar () {
                    Row(
                        horizontalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {

                        for (screen in bottomAppBarRowScreens) {
                            IconButton(onClick = { navController.navigate(screen.route) }) {
                                Icon(
                                    screen.icon,
                                    contentDescription = screen.route + "Icon",
                                    tint = Color.Green,
                                    modifier = Modifier
                                        .weight(1f)
                                )

                            }

                        }
                    }
                }
            }
        ) {
            mainNavHost(
                navController = navController,
                modifier = Modifier.padding(it))
        }

    }
}
