@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.example.hltv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.hltv.navigation.Home
import com.example.hltv.navigation.MainNavHost
import com.example.hltv.navigation.Settings
import com.example.hltv.navigation.bottomAppBarScreens
import com.example.hltv.ui.theme.HLTVTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HLTVApp()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HLTVApp() {
    HLTVTheme {
        val navController = rememberNavController()
        val currentBackStack by navController.currentBackStackEntryAsState()
        val currentDestination = currentBackStack?.destination
        val canNavigateBack = !bottomAppBarScreens.any { it.route == currentDestination?.route }
        val currentScreen =
            bottomAppBarScreens.find { it.route == currentDestination?.route } ?: Home

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = if (currentDestination?.route != Settings.route) {
                        { Text(text = currentScreen.route) }
                    } else {
                        { Text(text = "Settings") }
                    },
                    navigationIcon = if (canNavigateBack) {
                        {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Back-Arrow"
                                )
                            }
                        }
                    } else {
                        {}
                    },
                    actions = {
                        IconButton(onClick = { navController.navigate(Settings.route) }) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Settings Icon"
                            )
                        }
                    }
                )
            },
            bottomBar = {
                NavigationBar {
                    bottomAppBarScreens.forEach() { item ->
                        NavigationBarItem(
                            selected = currentScreen == item,
                            onClick = { navController.navigate(item.route) },
                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.route + "Icon",
                                    tint = if (currentScreen == item) Color.Red else Color.Green

                                )
                            },
                            label = { Text(text = item.route) })
                    }

                }

/* OLD BOTTOM APP BAR
                BottomAppBar() {
                    Row(
                        horizontalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        for (screen in bottomAppBarScreens) {

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
                }*/
            }
        ) {
            MainNavHost(
                navController = navController,
                modifier = Modifier.padding(it)
            )
        }
    }
}
private fun CreateTopBar(){

}
