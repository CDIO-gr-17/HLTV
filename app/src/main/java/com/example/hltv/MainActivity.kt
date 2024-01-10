@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.example.hltv

import android.annotation.SuppressLint
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.compose.HLTVTheme
import com.example.hltv.data.remote.getTeamNameFromID
import com.example.hltv.navigation.Home
import com.example.hltv.navigation.MainNavHost
import com.example.hltv.navigation.Settings
import com.example.hltv.navigation.SingleMatch
import com.example.hltv.navigation.SingleTeam
import com.example.hltv.navigation.allAppScreens
import com.example.hltv.navigation.bottomAppBarScreens
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HLTVApp()
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition", "UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HLTVApp() {
    HLTVTheme {
        val navController = rememberNavController()
        val currentBackStack by navController.currentBackStackEntryAsState()
        val currentDestination = currentBackStack?.destination
        val canNavigateBack = !bottomAppBarScreens.any { it.route == currentDestination?.route }
        val currentScreen = allAppScreens.find { it.route == currentDestination?.route } ?: Home



        Scaffold(topBar = {
            CenterAlignedTopAppBar(
                title = {
                    if (currentScreen == SingleTeam) {
                        var teamName by remember{ mutableStateOf("Team info")}
                        CoroutineScope(Dispatchers.IO).launch {
                            val teamID = currentBackStack?.arguments?.getString("teamID")
                            val teamIDInt = teamID?.toInt()
                            if (teamIDInt != null) {
                                teamName = getTeamNameFromID(teamIDInt)!!
                            } else {
                                teamName = "Team info"
                            }
                        }
                        Text(text = teamName)
                    } else if (currentScreen == SingleMatch) {

                    }
                    else {
                        Text(text = currentScreen.name)
                    }
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
                            contentDescription = "Settings Icon",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
            )
        }, bottomBar = {
            NavigationBar {
                bottomAppBarScreens.forEach { item ->
                    NavigationBarItem(selected = currentScreen == item,
                        onClick = { navController.navigate(item.route) },
                        icon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = item.icon),
                                contentDescription = item.route + "Icon",

                                )
                        },
                        label = { Text(text = item.route) })
                }

            }
        }) {
            MainNavHost(
                navController = navController, modifier = Modifier.padding(it)
            )
        }
    }
}
